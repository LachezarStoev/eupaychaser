package com.eupaychaser.service;

import com.eupaychaser.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${app.email.provider:demo}")
    private String provider;

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
        String message = "Email simulated successfully. Configure Mailgun/SendGrid in production.";
        return new SendEmailResponse(true, provider, message);
    }
}
