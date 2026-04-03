package com.eupaychaser.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class CountryRateService {
    private static final Map<String, BigDecimal> COUNTRY_RATES = Map.of(
            "BG", BigDecimal.valueOf(10.15),
            "DE", BigDecimal.valueOf(10.27),
            "FR", BigDecimal.valueOf(12.15)
    );

    public BigDecimal findRate(String countryCode) {
        if (countryCode == null || countryCode.isBlank()) {
            return BigDecimal.valueOf(10.0);
        }

        return COUNTRY_RATES.getOrDefault(countryCode.toUpperCase(), BigDecimal.valueOf(10.0));
    }

    public List<Map.Entry<String, BigDecimal>> supportedCountries() {
        return COUNTRY_RATES.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .toList();
    }
}
