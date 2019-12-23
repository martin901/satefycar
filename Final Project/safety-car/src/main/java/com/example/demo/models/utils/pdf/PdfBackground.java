package com.example.demo.models.utils.pdf;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

import java.io.IOException;

public class PdfBackground extends PdfPageEventHelper {

    public void onEndPage(PdfWriter writer, Document document) {
        Image background = null;
        try {
            background = Image.getInstance("src/main/resources/static/images/print-backg.jpg");
        } catch (BadElementException | IOException e) {
            e.printStackTrace();
        }
        float width = document.getPageSize().getWidth();
        float height = document.getPageSize().getHeight();
        try {
            writer.getDirectContentUnder()
                    .addImage(background, width, 0, 0, height, 0, 0);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
