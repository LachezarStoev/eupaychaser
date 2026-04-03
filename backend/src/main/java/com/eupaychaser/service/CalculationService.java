package com.eupaychaser.service;

import com.eupaychaser.dto.CalculationResponse;
import com.eupaychaser.dto.CaseRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
public class CalculationService {
    private static final BigDecimal FIXED_FEE = BigDecimal.valueOf(40);
    private static final Map<String, BigDecimal> COUNTRY_RATES = Map.of(
            "BG", BigDecimal.valueOf(10.15),
            "DE", BigDecimal.valueOf(10.27),
            "FR", BigDecimal.valueOf(12.15)
    );

    public CalculationResponse calculate(CaseRequest request) {
        String country = request.country().toUpperCase();
        BigDecimal rate = COUNTRY_RATES.getOrDefault(country, BigDecimal.valueOf(10.0));

        long lateDays = Math.max(0, ChronoUnit.DAYS.between(request.dueDate(), LocalDate.now()));

        BigDecimal interest = request.amount()
                .multiply(rate)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(lateDays))
                .divide(BigDecimal.valueOf(365), 2, RoundingMode.HALF_UP);

        BigDecimal total = request.amount().add(interest).add(FIXED_FEE).setScale(2, RoundingMode.HALF_UP);

        return new CalculationResponse(
                lateDays,
                rate.setScale(2, RoundingMode.HALF_UP),
                interest,
                FIXED_FEE,
                total
        );
    }
}
