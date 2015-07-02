package com.stfciz.mmc.web.api.misc;

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
@Component("miscApiConverter")
public class MiscApiConverter extends AbstractApiConverter<GetResponse, SaveRequest> {

  @Autowired
  public MiscApiConverter(PhotoApiConverter photoApiConverter) {
    super(photoApiConverter);
  }

  @Override
  public GetResponse newGetResponse() {
    return new GetResponse();
  }

  @Override
  protected MMCDocument populateFromSaveRequest(MMCDocument target, SaveRequest request) {
    if (request.getGlobalRating() != null && request.getGlobalRating() > 0) {
      target.setGlobalRating(request.getGlobalRating());
    }
    return target;
  }

  @Override
  public DocumentType getDocumentType() {
    return DocumentType.MISC;
  }
}
