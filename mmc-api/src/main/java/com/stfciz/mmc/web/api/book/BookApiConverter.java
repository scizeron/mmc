package com.stfciz.mmc.web.api.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stfciz.mmc.core.domain.DocumentType;
import com.stfciz.mmc.core.domain.MMCDocument;
import com.stfciz.mmc.web.api.AbstractApiConverter;
import com.stfciz.mmc.web.api.GetResponse;
import com.stfciz.mmc.web.api.photo.PhotoApiConverter;

/**
 * 
 * @author stfciz
 *
 * 2 juil. 2015
 */
@Component("bookApiConverter")
public class BookApiConverter extends AbstractApiConverter<GetResponse, SaveRequest> {

  @Autowired
  public BookApiConverter(PhotoApiConverter photoApiConverter) {
    super(photoApiConverter);
  }

  @Override
  public GetResponse newGetResponse() {
    return new GetResponse();
  }

  @Override
  protected MMCDocument populateFromSaveRequest(MMCDocument target, SaveRequest request) {
    target.setAuthor(request.getAuthor());
    target.setDistributer(request.getDistributer());
    target.setPublisher(request.getPublisher());
    target.setIsbn(request.getIsbn());
    target.setNbPages(request.getNbPages());
    
    if (request.getGlobalRating() != null && request.getGlobalRating() > 0) {
      target.setGlobalRating(request.getGlobalRating());
    }
    return target;
  }

  @Override
  public DocumentType getDocumentType() {
    return DocumentType.BOOK;
  }
}
