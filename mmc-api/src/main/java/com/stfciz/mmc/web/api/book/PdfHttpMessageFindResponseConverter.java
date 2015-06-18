package com.stfciz.mmc.web.api.book;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter;

/**
 * 
 * @author Bellevue
 *
 * @param <T>
 */
public class PdfHttpMessageFindResponseConverter extends AbstractPdfHttpMessageFindResponseConverter<FindResponse> {
  
  private static final float[] RELATIVE_WIDTHS = new float[] { 3, 6, 2, 2, 2, 2 };
  
  @Override
  protected boolean supports(Class<?> clazz) {
    return clazz.isAssignableFrom(FindResponse.class);
  }

  @Override
  protected void writeInternal(FindResponse findResponse,
      HttpOutputMessage outputMessage) throws IOException,
      HttpMessageNotWritableException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    OutputStream os = outputMessage.getBody();
    try {
      Document document = new Document();
      PdfWriter.getInstance(document, baos);
      document.open();

      PdfPTable table = new PdfPTable(RELATIVE_WIDTHS);
      table.setWidthPercentage(100f);
      table.getDefaultCell().setUseAscender(true);
      table.getDefaultCell().setUseDescender(true);
      // Add the first header row
      Font f = new Font();
      f.setColor(BaseColor.WHITE);
      PdfPCell cell = new PdfPCell(new Phrase(String.format("%s - %d item(s)",
            new SimpleDateFormat("yyyy/MM/dd").format(new Date())
          , findResponse.getItems().size()),  f));
      cell.setBackgroundColor(BaseColor.BLACK);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setColspan(RELATIVE_WIDTHS.length);
      table.addCell(cell);

      // Add the second header row twice
      for (int i = 0; i < 2; i++) {
        addCell(table, "Author", BaseColor.LIGHT_GRAY);
        addCell(table, "Title", BaseColor.LIGHT_GRAY);
        addCell(table, "Origin", BaseColor.LIGHT_GRAY);
        addCell(table, "Year", BaseColor.LIGHT_GRAY);
        addCell(table, "Edition", BaseColor.LIGHT_GRAY);
        addCell(table, "Rating", BaseColor.LIGHT_GRAY);
      }
      
      table.getDefaultCell().setBackgroundColor(null);
      // There are three special rows
      table.setHeaderRows(3);
      // One of them is a footer
      table.setFooterRows(1);
      // Now let's loop over the elements

      List<FindElementResponse> docs = findResponse.getItems();
      for (FindElementResponse doc : docs) {
        addCell(table, doc.getAuthor());
        addCell(table, doc.getTitle());
        addCell(table, doc.getOrigin());
        addCell(table, doc.getIssue());

        StringBuilder edition = new StringBuilder();
        
        if (doc.isReEdition()) {
          edition.append("reedition");
        }

        if (StringUtils.isNotBlank(doc.getIsbn())) {
          if (edition.length() > 0) {
            edition.append(", ");
          }
          edition.append("ISBN ").append(doc.getIsbn());
        }
        
        if (doc.getPubNum() != null) {
          if (edition.length() > 0) {
            edition.append(", ");
          }
          edition.append(doc.getPubNum()).append("/").append(doc.getPubTotal());
        }
        
        if (doc.isPromo()) {
          if (edition.length() > 0) {
            edition.append(", ");
          }
          edition.append("promo");
        }

        addCell(table, edition.toString());
        addCell(table, formatRatingValue(doc.getGlobalRating()));
      }

      document.add(table);
      document.close();

      StreamUtils.copy(baos.toByteArray(), os);
    } catch (DocumentException e) {
      throw new HttpMessageNotWritableException("Write internal error", e);
    }
  }
}
