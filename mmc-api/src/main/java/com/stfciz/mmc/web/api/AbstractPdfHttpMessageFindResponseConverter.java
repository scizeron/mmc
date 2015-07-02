package com.stfciz.mmc.web.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.stfciz.mmc.web.api.book.PdfHttpMessageFindResponseConverter;

/**
 * 
 * @author stfciz
 *
 * 18 juin 2015
 */
public abstract class AbstractPdfHttpMessageFindResponseConverter extends AbstractHttpMessageConverter<FindResponse> {
 
  protected static final Logger LOGGER = LoggerFactory
      .getLogger(PdfHttpMessageFindResponseConverter.class);

  protected static final String NO_VALUE = "";

  protected static final ResourceBundle RATINGS = ResourceBundle
      .getBundle("mapping/ratings");

  protected static final ResourceBundle COLORS = ResourceBundle
      .getBundle("mapping/colors");
  
  /**
   * 
   */
  public AbstractPdfHttpMessageFindResponseConverter() {
    super(MediaType.valueOf("application/pdf"));
  }
  
  @Override
  protected boolean supports(Class<?> clazz) {
    return clazz.isAssignableFrom(FindResponse.class);
  }
  
  @Override
  protected FindResponse readInternal(Class<? extends FindResponse> clazz,
      HttpInputMessage inputMessage) throws IOException,
      HttpMessageNotReadableException {
    return null;
  }
  
  /**
   * 
   * @author stfciz
   *
   * 19 juin 2015
   */
  protected static class Column {
    private long relativeWitdh;
    private String name;
    /**
     * 
     * @param name
     * @param relativeWidth
     */
    public Column(String name, long relativeWidth) {
      this.name = name;
      this.relativeWitdh = relativeWidth;
    }
    
    public long getRelativeWitdh() {
      return relativeWitdh;
    }
    
    public String getName() {
      return name;
    }
  }
  
  /**
   * 
   * @return
   */
  protected abstract Column[] getColumns();
  
  /**
   * 
   * @param table
   * @param value
   * @param font
   */
  protected void addCell(PdfPTable table, Object value, BaseColor cellBackgroundColor) {
    PdfPCell cell = new PdfPCell(new Phrase(formatObjectValue(value)));
    if (cellBackgroundColor != null) {
      cell.setBackgroundColor(cellBackgroundColor);
    }
    table.addCell(cell);
  }

  /**
   * 
   * @param table
   * @param value
  */
  protected void addCell(PdfPTable table, Object value) {
     addCell(table, value, null);
   }
  
  /**
   * 
   * @param value
   * @return
   */
  protected String formatObjectValue(Object value) {
    return (value != null) ? String.valueOf(value) : NO_VALUE;
  }

  /**
   * 
   * @param value
   * @return
   */
  protected String formatRatingValue(Integer value) {
    return (value != null) ? RATINGS.getString(String.valueOf(value))
        : NO_VALUE;
  }
  
  /**
   * 
   * @param colorStr
   * @return
   */
  protected String getColorLabel(String colorStr) {
    return getColorLabel(colorStr, null);
  }

  /**
   * 
   * @param colorStr
   * @param exclude
   * @return
   */
  protected String getColorLabel(String colorStr, String exclude) {
    try {
      String color = StringUtils.remove(colorStr, "#").toLowerCase();
      if (exclude != null && exclude.equals(colorStr)) {
        return null;
      }
      return COLORS.getString(color.toLowerCase());
    } catch (Exception e) {
      LOGGER.error("Error when getting label color of {}", colorStr, e);
      return null;
    }
  }
  
  /**
   * 
   * @param findResponse
   * @param table
   */
  protected abstract void addItems(FindResponse findResponse, PdfPTable table);
  
  @Override
  protected void writeInternal(FindResponse findResponse, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    OutputStream os = outputMessage.getBody();
    try {
      Document document = new Document();
      PdfWriter.getInstance(document, baos);
      document.open();
      
      Column [] cols = getColumns();
      float [] relativeWidths = new float[cols.length];
      for (int i = 0; i < relativeWidths.length; i++) {
        relativeWidths[i] = cols[i].getRelativeWitdh();
      }
      
      PdfPTable table = new PdfPTable(relativeWidths);
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
      cell.setColspan(cols.length);
      table.addCell(cell);
      
      // Add the second header row twice
      for (int i = 0; i < 2; i++) {
        for (int j = 0; j < getColumns().length; j++) {
          addCell(table, cols[j].getName(), BaseColor.LIGHT_GRAY);
        }
      }
      
      table.getDefaultCell().setBackgroundColor(null);
      // There are three special rows
      table.setHeaderRows(3);
      // One of them is a footer
      table.setFooterRows(1);
      
      // Now let's loop over the elements
      addItems(findResponse, table);

      document.add(table);
      document.close();

      StreamUtils.copy(baos.toByteArray(), os);
            
    } catch (DocumentException e) {
      throw new HttpMessageNotWritableException("Write internal error", e);
    }      
  }
}
