package com.stfciz.mmc.web.api.misc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stfciz.mmc.core.misc.domain.MiscDocument;
import com.stfciz.mmc.web.api.AbstractApiConverter;
import com.stfciz.mmc.web.api.photo.PhotoApiConverter;

@Component("miscApiConverter")
public class MiscApiConverter extends AbstractApiConverter<MiscDocument, GetResponse, NewRequest, UpdateRequest, FindElementResponse, FindResponse> {

  @Autowired
  public MiscApiConverter(PhotoApiConverter photoApiConverter) {
    super(photoApiConverter);
  }

  @Override
  public MiscDocument newDocument() {
    return new MiscDocument();
  }
  
  @Override
  public FindResponse newFindResponse() {
    return new FindResponse();
  }

  @Override
  public FindElementResponse newFindElementResponse() {
    return new FindElementResponse();
  }
  
  @Override
  public GetResponse newGetResponse() {
    return new GetResponse();
  }
  
  @Override
  protected MiscDocument populateSpecificInfosFromNewRequest(
      MiscDocument target, NewRequest request) {
    target.setDescription(request.getDescription());
    target.setTitle(request.getTitle());
    
    if (request.getGlobalRating() != null && request.getGlobalRating() > 0) {
      target.setGlobalRating(request.getGlobalRating());
    }
    return target;
  }
  
  @Override
  protected GetResponse populateGetResponseWithSpecificInfos(MiscDocument doc,
      GetResponse target) {
    populateAbstractMiscBaseResponse(doc, target);
    return target;
  }
  
  
  @Override
  protected MiscDocument populateSpecificInfosFromUpdateRequest(
      MiscDocument target, UpdateRequest request) {
    return populateSpecificInfosFromNewRequest(target, request);
  }

  @Override
  protected FindElementResponse populateFindElementResponseWithSpecificInfos(
      MiscDocument doc, FindElementResponse target) {
    populateAbstractMiscBaseResponse(doc, target);
    return target;
  }
  
  /**
   * 
   * @param src
   * @param target
   */
  private void populateAbstractMiscBaseResponse(MiscDocument doc, AbstractMiscBaseResponse target) {
    target.setTitle(doc.getTitle());
    target.setDescription(doc.getDescription());
    target.setGlobalRating(doc.getGlobalRating());
  }

}
