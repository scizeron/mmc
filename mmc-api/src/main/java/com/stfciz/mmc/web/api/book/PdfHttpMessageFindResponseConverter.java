package com.stfciz.mmc.web.api.book;

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
  protected com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column[] getColumns() {
    return new com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column[] {
           new com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column("Author", 3),
           new com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column("Title", 6),
           new com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column("Origin", 2),
           new com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column("Year", 2),
           new com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column("Edition", 2),
           new com.stfciz.mmc.web.api.AbstractPdfHttpMessageFindResponseConverter.Column("Rating", 2)
    };
  }

  @Override
  protected void addItems(FindResponse findResponse, PdfPTable table) {
    List<FindItemResponse> docs = findResponse.getItems();
    for (FindItemResponse doc : docs) {
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
  }

}
