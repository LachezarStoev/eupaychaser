package com.eupaychaser.service;

import com.eupaychaser.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailService {
    private final WebClient webClient;

    @Value("${app.email.provider:demo}")
    private String provider;

    @Value("${app.email.provider-url:}")
    private String providerUrl;

    public EmailService(WebClient emailWebClient) {
        this.webClient = emailWebClient;
    }

    public EmailPreviewResponse preview(CaseRequest request, CalculationResponse calculation) {
        String subject = "Payment Reminder – Overdue Invoice";
        String body = String.format("""
                Dear client,

                This is a reminder that your invoice is now overdue.
                Under Directive 2011/7/EU, statutory late-payment charges now apply.

                Breakdown:
                - Original amount: €%s
                - Interest: €%s
                - Fixed compensation: €%s
                - Total claim: €%s

                Please find the attached notice.

                Regards,
                Accounts Department
                """, request.amount(), calculation.interest(), calculation.fixedFee(), calculation.totalClaim());

        return new EmailPreviewResponse(subject, body);
    }

    public SendEmailResponse send(SendEmailRequest request) {
        if (providerUrl == null || providerUrl.isBlank()) {
            return new SendEmailResponse(true, provider, "Email simulated successfully. Configure Mailgun/SendGrid in production.");
        }

        webClient.post()
                .uri(providerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "to", request.debtorEmail(),
                        "subject", request.subject(),
                        "body", request.body()
                ))
                .retrieve()
                .toBodilessEntity()
                .block();

        return new SendEmailResponse(true, provider, "Email sent using mocked provider endpoint.");
    }
}
