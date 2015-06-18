package com.stfciz.mmc.web.api.music;

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

  private static final float[] RELATIVE_WIDTHS = new float[] { 3, 6, 2, 2, 2, 3, 2, 2 };

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
        addCell(table, "Artist", BaseColor.LIGHT_GRAY);
        addCell(table, "Title", BaseColor.LIGHT_GRAY);
        addCell(table, "Type", BaseColor.LIGHT_GRAY);
        addCell(table, "Origin", BaseColor.LIGHT_GRAY);
        addCell(table, "Year", BaseColor.LIGHT_GRAY);
        addCell(table, "Edition", BaseColor.LIGHT_GRAY);
        addCell(table, "Sleeve", BaseColor.LIGHT_GRAY);
        addCell(table, "Record", BaseColor.LIGHT_GRAY);
      }
      
      table.getDefaultCell().setBackgroundColor(null);
      // There are three special rows
      table.setHeaderRows(3);
      // One of them is a footer
      table.setFooterRows(1);
      // Now let's loop over the elements

      List<FindElementResponse> docs = findResponse.getItems();
      for (FindElementResponse doc : docs) {
        addCell(table, doc.getArtist());
        addCell(table, doc.getTitle());
        table.addCell(String.format("%s %s", (doc.getNbType() != null && doc
            .getNbType() > 1) ? String.valueOf(doc.getNbType()) : NO_VALUE, doc
            .getMainType()));
        
        
        if ("JP".equals(doc.getOrigin())) {
          StringBuilder origin = new StringBuilder();
          if (StringUtils.isNotBlank(doc.getOrigin())) {
            origin.append(doc.getOrigin()); 
          }
          
          if (StringUtils.isNotBlank(doc.getObiColor())
              && StringUtils.isNotBlank(doc.getObiPos())) {
            String obiColor = getColorLabel(doc.getObiColor());
            if (obiColor != null) {
              if (origin.length() > 0) {
                origin.append(" [");
              }
              origin.append("obi:").append(doc.getObiPos()).append("-").append(obiColor).append("]");
            }
            
            addCell(table, origin);
            
          } else {
            addCell(table, doc.getOrigin());
          }
        } else {
          addCell(table, doc.getOrigin());
        }
       
        
        addCell(table, doc.getIssue());

        StringBuilder edition = new StringBuilder();
        
        if (doc.isReEdition()) {
          edition.append("reedition");
        }

        if (StringUtils.isNotBlank(doc.getSerialNumber())) {
          if (edition.length() > 0) {
            edition.append(", ");
          }
          edition.append("N° ").append(doc.getSerialNumber());
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

        if (StringUtils.isNotBlank(doc.getVinylColor())) {
          String colorLabel = getColorLabel(doc.getVinylColor(), "000000");
          if (colorLabel != null) {
            if (edition.length() > 0) {
              edition.append(", ");
            }
            edition./*append("disc: ").*/append(colorLabel);
          }
        }

        addCell(table, edition.toString());
        addCell(table, formatRatingValue(doc.getSleeveGrade()));
        addCell(table, formatRatingValue(doc.getRecordGrade()));
      }

      document.add(table);
      document.close();

      StreamUtils.copy(baos.toByteArray(), os);
    } catch (DocumentException e) {
      throw new HttpMessageNotWritableException("Write internal error", e);
    }
  }
}
