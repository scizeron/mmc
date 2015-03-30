package com.stfciz.mmc.web.api.music;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

/**
 * 
 * @author Bellevue
 *
 * @param <T>
 */
public class PdfHttpMessageFindResponseConverter extends AbstractHttpMessageConverter<FindResponse> {

  private static final String NO_VALUE = "";
  private ResourceBundle rating = ResourceBundle.getBundle("rating");
  
  /**
   * 
   */
  public PdfHttpMessageFindResponseConverter() {
    super(MediaType.valueOf("application/pdf"));  
  }
  
  @Override
  protected boolean supports(Class<?> clazz) {
    return true;
  }

  @Override
  protected FindResponse readInternal(Class<? extends FindResponse> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
    return null;
  }

  @Override
  protected void writeInternal(FindResponse findResponse, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    OutputStream os = outputMessage.getBody();
    try {
      Document document = new Document();
      PdfWriter.getInstance(document, baos);
      document.open();

      PdfPTable table = new PdfPTable(new float[] { 3, 5, 1, 2, 1, 1, 1, 1 });
      table.setWidthPercentage(100f);
      table.getDefaultCell().setUseAscender(true);
      table.getDefaultCell().setUseDescender(true);
      // Add the first header row
      Font f = new Font();
      f.setSize(8);
      f.setColor(BaseColor.WHITE);
      PdfPCell cell = new PdfPCell(new Phrase(String.format("%s - %d item(s)"
          , new SimpleDateFormat("yyyy/MM/dd").format(new Date())
          , findResponse.getDocs().size()), f));
      cell.setBackgroundColor(BaseColor.BLACK);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setColspan(8);
      table.addCell(cell);
      // Add the second header row twice
      table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
      for (int i = 0; i < 2; i++) {
          table.addCell("Artist");
          table.addCell("Title");
          table.addCell("Type");
          table.addCell("Origin");
          table.addCell("Year");
          table.addCell("Re-edition");
          table.addCell("Sleeve");
          table.addCell("Record");
      }
      table.getDefaultCell().setBackgroundColor(null);
      // There are three special rows
      table.setHeaderRows(3);
      // One of them is a footer
      table.setFooterRows(1);
      // Now let's loop over the elements
      
      List<FindElementResponse> docs = findResponse.getDocs();
      for (FindElementResponse doc : docs) {
        table.addCell(doc.getArtist());
        table.addCell(doc.getTitle());
        table.addCell(String.format("%s %s", (doc.getNbType() != null && doc.getNbType() > 1 ) ? String.valueOf(doc.getNbType()) : NO_VALUE, doc.getMainType()));
        table.addCell(formatStringValue(doc.getOrigin()));
        if (doc.getEdition() != null) {
          table.addCell(formatIntegerValue(doc.getEdition()));
          table.addCell(String.format("%s", "y"));
        } else {
          table.addCell(formatIntegerValue(doc.getIssue()));
          table.addCell(NO_VALUE);
        }
        table.addCell(formatRating(doc.getSleeveGrade()));
        table.addCell(formatRating(doc.getRecordGrade()));
      }
      
      document.add(table);
      document.close();
      
      StreamUtils.copy(baos.toByteArray(), os);
    } catch (DocumentException e) {
      throw new HttpMessageNotWritableException("Write internal error", e);
    }
  }
  
  private String formatRating(Integer value) {
    if (value != null) {
      return rating.getString(String.valueOf(value));
    }
    return NO_VALUE;
  }
  
  private String formatIntegerValue(Integer value) {
    if (value != null) {
      return String.format("%d", value);
    }
    return NO_VALUE;
  }
  
  private String formatStringValue(String value) {
    if (value != null) {
      return value;
    }
    return NO_VALUE;
  }
}
