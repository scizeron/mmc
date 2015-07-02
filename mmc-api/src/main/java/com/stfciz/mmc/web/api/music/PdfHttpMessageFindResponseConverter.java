package com.stfciz.mmc.web.api.music;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.pdf.PdfPTable;
import com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter;
import com.stfciz.mmc.web.api.FindItemResponse;
import com.stfciz.mmc.web.api.FindResponse;

/**
 * 
 * @author Bellevue
 *
 * @param <T>
 */
public class PdfHttpMessageFindResponseConverter extends AbstractPdfHttpMessageFindResponseConverter {

  @Override
  protected boolean supports(Class<?> clazz) {
    return clazz.isAssignableFrom(FindResponse.class);
  }

  @Override
  protected com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column[] getColumns() {
    return new com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column[] {
        new com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column("Artist", 3),
        new com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column("Title", 6),
        new com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column("Type", 2),
        new com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column("Origin", 2),
        new com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column("Year", 2),
        new com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column("Edition", 2),
        new com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column("Sleeve", 2),
        new com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column("Sleeve", 2)
    };
  }

  @Override
  protected void addItems(FindResponse findResponse, PdfPTable table) {
    List<FindItemResponse> docs = findResponse.getItems();
    for (FindItemResponse doc : docs) {
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
  }
}
