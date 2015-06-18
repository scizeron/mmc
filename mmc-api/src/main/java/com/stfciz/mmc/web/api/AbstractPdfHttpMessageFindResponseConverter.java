package com.stfciz.mmc.web.api;

import java.io.IOException;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.stfciz.mmc.web.api.book.PdfHttpMessageFindResponseConverter;

/**
 * 
 * @author stfciz
 *
 * 18 juin 2015
 */
public abstract class AbstractPdfHttpMessageFindResponseConverter<T> extends AbstractHttpMessageConverter<T> {
 
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
  protected T readInternal(Class<? extends T> clazz,
      HttpInputMessage inputMessage) throws IOException,
      HttpMessageNotReadableException {
    return null;
  }
  
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
}
