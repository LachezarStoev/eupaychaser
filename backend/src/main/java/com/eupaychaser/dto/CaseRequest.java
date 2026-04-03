package com.eupaychaser.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CaseRequest(
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotNull LocalDate dueDate,
        @NotBlank @Email String debtorEmail,
        @NotBlank String country
) {
}
