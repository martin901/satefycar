package com.example.demo.models.utils.pdf;

import com.example.demo.models.PolicyRequest;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

@Component("pdfView")
public class PdfView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PolicyRequest policyRequest = (PolicyRequest) model.get("policyrequest");

        writer.setPageEvent(new PdfBackground());

        document.addTitle("Request");
        document.addCreationDate();
        document.addCreator(policyRequest.getUser().getName().toString());

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.LLLL.yyyy");

        Image logo = Image.getInstance("src/main/resources/static/images/logo.png");
        document.add(logo);

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));

        Font headingFont = new Font(Font.BOLD, 18);
        LocalDate requestDate = convertToLocalDateViaInstant(policyRequest.getRequestDate());
        Paragraph heading = new Paragraph("Request No. " + policyRequest.getId() + "/" + requestDate.format(dateFormatter), headingFont);
        heading.setAlignment(Element.ALIGN_CENTER);
        document.add(heading);

        Font bold = new Font(Font.BOLD);

        Paragraph carInfoPar = new Paragraph("Car Information", bold);
        carInfoPar.setAlignment(Element.ALIGN_CENTER);
        document.add(new Paragraph("\n"));
        document.add(carInfoPar);
        document.add(new Paragraph("\n"));
        PdfPTable carInfoTable = new PdfPTable(2);

        carInfoTable.addCell("Car");
        carInfoTable.addCell(policyRequest.getOffer().getCarModel().toString());

        carInfoTable.addCell("Cubic Capacity");
        carInfoTable.addCell(policyRequest.getOffer().getCubicCapacity().toString());

        carInfoTable.addCell("First Registraton");
        carInfoTable.addCell(policyRequest.getOffer().getFirstRegDate().format(dateFormatter));

        document.add(carInfoTable);
        Paragraph requesterPar = new Paragraph("Requester", bold);
        requesterPar.setAlignment(Element.ALIGN_CENTER);
        document.add(new Paragraph("\n"));
        document.add(requesterPar);
        document.add(new Paragraph("\n"));

        PdfPTable requesterInfoTable = new PdfPTable(2);

        requesterInfoTable.addCell("Name");
        requesterInfoTable.addCell(policyRequest.getUser().getName().toString());

        requesterInfoTable.addCell("Address");
        requesterInfoTable.addCell(policyRequest.getUser().getAddress().getAddress());

        requesterInfoTable.addCell("Age");
        requesterInfoTable.addCell(policyRequest.getOffer().getDriverAge().toString());

        requesterInfoTable.addCell("Had Accident");
        requesterInfoTable.addCell(policyRequest.getOffer().isHadAccident() ? "Yes" : "No");

        document.add(requesterInfoTable);
        Paragraph policyInfoPar = new Paragraph("Policy Information", bold);
        policyInfoPar.setAlignment(Element.ALIGN_CENTER);
        document.add(new Paragraph("\n"));
        document.add(policyInfoPar);
        document.add(new Paragraph("\n"));

        PdfPTable requestInfoTable = new PdfPTable(2);

        requestInfoTable.addCell("Effective Date");
        requestInfoTable.addCell(policyRequest.getEffectiveDate().format(dateFormatter));

        requestInfoTable.addCell("Status");
        requestInfoTable.addCell(policyRequest.getRequestStatus().getViewName());

        requestInfoTable.addCell(policyRequest.getRequestStatus().getViewName() + " by");
        requestInfoTable.addCell(policyRequest.getProcessedBy().getName().toString());

        requestInfoTable.addCell("Total Premium");
        requestInfoTable.addCell(policyRequest.getOffer().getTotalPremium().toString() + " BGN");

        document.add(requestInfoTable);
        document.add(new Paragraph("\n"));

    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
