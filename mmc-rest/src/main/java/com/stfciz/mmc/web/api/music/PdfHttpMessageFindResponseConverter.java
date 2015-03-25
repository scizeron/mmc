package com.stfciz.mmc.web.api.music;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 
 * @author Bellevue
 *
 * @param <T>
 */
public class PdfHttpMessageFindResponseConverter extends AbstractHttpMessageConverter<FindResponse> {

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
    Document document = new Document();
    OutputStream output = outputMessage.getBody();
    try {
      PdfWriter.getInstance(document, output);
      document.open();
      document.add(new Paragraph("Hello World!"));
      document.close();
    } catch (DocumentException e) {
      throw new HttpMessageNotWritableException("Write internal error", e);
    }
  }
}
