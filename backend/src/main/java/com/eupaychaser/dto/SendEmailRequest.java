package com.eupaychaser.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendEmailRequest(
        @NotBlank @Email String debtorEmail,
        @NotBlank String subject,
        @NotBlank String body
) {
}
