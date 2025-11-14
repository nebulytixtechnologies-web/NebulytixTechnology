package com.neb.util;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.neb.entity.Work;

public class ReportGeneratorPdf {

	public byte[] generateDailyReportPDF(List<Work> works,LocalDate date) throws Exception {
		
	    if (works == null || works.isEmpty()) {
	        // optional: still create PDF with “no data” message
	    }

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    // Use landscape page size: A4.rotate()
	    Document document = new Document(PageSize.A4.rotate(), 20f, 20f, 20f, 20f);
	    PdfWriter.getInstance(document, baos);
	    document.open();

	    // Title / header row
	    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
	    Paragraph title = new Paragraph("Daily Report", titleFont);
	    title.setAlignment(Element.ALIGN_LEFT);
	    document.add(title);

	    Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
	    Paragraph datePara = new Paragraph("Submitted Date: " + date.toString(), dateFont);
	    datePara.setAlignment(Element.ALIGN_RIGHT);
	    document.add(datePara);

	    document.add(Chunk.NEWLINE);

	    // Create table with 6 columns
	    PdfPTable table = new PdfPTable(6);
	    table.setWidthPercentage(100f);
	    table.setSpacingBefore(10f);
	    table.setSpacingAfter(10f);

	    // Set column widths (you can tweak these)
	    float[] columnWidths = {2f, 3f, 3f, 4f, 2f, 6f};
	    table.setWidths(columnWidths);

	    // Header cells
	    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
	    table.addCell(new PdfPCell(new Phrase("Emp Card No", headerFont)));
	    table.addCell(new PdfPCell(new Phrase("Emp Name", headerFont)));
	    table.addCell(new PdfPCell(new Phrase("Emp Role", headerFont)));
	    table.addCell(new PdfPCell(new Phrase("Task Title", headerFont)));
	    table.addCell(new PdfPCell(new Phrase("Status", headerFont)));
	    table.addCell(new PdfPCell(new Phrase("Report Details", headerFont)));
	    table.setHeaderRows(1); // ensures header is repeated on new pages

	    // Fill table rows
	    Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
	    for (Work w : works) {
	        String cardNo = w.getEmployee().getCardNumber();
	        String name = w.getEmployee().getFirstName() + " " + w.getEmployee().getLastName();
	        String role = w.getEmployee().getJobRole();            // adjust if your entity uses different field
	        String taskTitle = w.getTitle();
	        String status = w.getStatus() != null ? w.getStatus().name() : "";
	        String reportDetails = w.getReportDetails() != null ? w.getReportDetails() : "";

	        table.addCell(new PdfPCell(new Phrase(cardNo, rowFont)));
	        table.addCell(new PdfPCell(new Phrase(name, rowFont)));
	        table.addCell(new PdfPCell(new Phrase(role, rowFont)));
	        table.addCell(new PdfPCell(new Phrase(taskTitle, rowFont)));
	        table.addCell(new PdfPCell(new Phrase(status, rowFont)));
	        table.addCell(new PdfPCell(new Phrase(reportDetails, rowFont)));
	    }
	    document.add(table);
	    document.close();

	    return baos.toByteArray();
	}
}
