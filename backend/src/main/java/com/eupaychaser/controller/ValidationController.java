package com.eupaychaser.controller;

import com.eupaychaser.dto.*;
import com.eupaychaser.service.CalculationService;
import com.eupaychaser.service.EmailService;
import com.eupaychaser.service.PdfService;
import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ValidationController {
    private final CalculationService calculationService;
    private final PdfService pdfService;
    private final EmailService emailService;

    public ValidationController(CalculationService calculationService, PdfService pdfService, EmailService emailService) {
        this.calculationService = calculationService;
        this.pdfService = pdfService;
        this.emailService = emailService;
    }

    @PostMapping("/calculate")
    public CalculationResponse calculate(@Valid @RequestBody CaseRequest request) {
        return calculationService.calculate(request);
    }

    @PostMapping("/pdf")
    public ResponseEntity<byte[]> generatePdf(@Valid @RequestBody CaseRequest request) {
        CalculationResponse calculation = calculationService.calculate(request);
        byte[] content = pdfService.generateNotice(request, calculation);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("late-payment-notice.pdf").build());

        return ResponseEntity.ok().headers(headers).body(content);
    }

    @PostMapping("/email/preview")
    public EmailPreviewResponse previewEmail(@Valid @RequestBody CaseRequest request) {
        CalculationResponse calculation = calculationService.calculate(request);
        return emailService.preview(request, calculation);
    }

    @PostMapping("/email/send")
    public SendEmailResponse sendEmail(@Valid @RequestBody SendEmailRequest request) {
        return emailService.send(request);
    }
}
