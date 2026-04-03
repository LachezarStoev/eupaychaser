package com.eupaychaser.dto;

import java.math.BigDecimal;

public record CalculationResponse(
        long lateDays,
        BigDecimal rate,
        BigDecimal interest,
        BigDecimal fixedFee,
        BigDecimal totalClaim
) {
}
