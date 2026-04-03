package com.eupaychaser.service;

import com.eupaychaser.dto.CalculationResponse;
import com.eupaychaser.dto.CaseRequest;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {
    public byte[] generateNotice(CaseRequest request, CalculationResponse calculation) {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);

        document.add(new Paragraph("Late Payment Notice", titleFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Debtor: " + request.debtorEmail()));
        document.add(new Paragraph("Invoice amount: €" + request.amount()));
        document.add(new Paragraph("Due date: " + request.dueDate()));
        document.add(new Paragraph("Country: " + request.country().toUpperCase()));
        document.add(new Paragraph("Late days: " + calculation.lateDays()));
        document.add(new Paragraph("Statutory rate: " + calculation.rate() + "%"));
        document.add(new Paragraph("Interest: €" + calculation.interest()));
        document.add(new Paragraph("Fixed compensation: €" + calculation.fixedFee()));
        document.add(new Paragraph("Total claim amount: €" + calculation.totalClaim()));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("This document is generated for informational/demo purposes."));

        document.close();
        return outputStream.toByteArray();
    }
}
