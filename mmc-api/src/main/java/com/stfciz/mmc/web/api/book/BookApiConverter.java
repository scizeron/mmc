package com.stfciz.mmc.web.api.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stfciz.mmc.core.book.domain.BookDocument;
import com.stfciz.mmc.web.api.AbstractApiConverter;
import com.stfciz.mmc.web.api.photo.PhotoApiConverter;

@Component("bookApiConverter")
public class BookApiConverter extends AbstractApiConverter<BookDocument, GetResponse, NewRequest, UpdateRequest, FindElementResponse, FindResponse> {

  @Autowired
  public BookApiConverter(PhotoApiConverter photoApiConverter) {
    super(photoApiConverter);
  }

  @Override
  public BookDocument newDocument() {
    return new BookDocument();
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
  protected BookDocument populateSpecificInfosFromNewRequest(
      BookDocument target, NewRequest request) {
    target.setAuthor(request.getAuthor());
    target.setDescription(request.getDescription());
    target.setDistributer(request.getDistributer());
    target.setIsbn(request.getIsbn());
    target.setTitle(request.getTitle());
    target.setNbPages(request.getNbPages());
    
    if (request.getGlobalRating() != null && request.getGlobalRating() > 0) {
      target.setGlobalRating(request.getGlobalRating());
    }
    return target;
  }
  
  @Override
  protected GetResponse populateGetResponseWithSpecificInfos(BookDocument doc,
      GetResponse target) {
    populateAbstractBookBaseResponse(doc, target);
    return target;
  }
  
  
  @Override
  protected BookDocument populateSpecificInfosFromUpdateRequest(
      BookDocument target, UpdateRequest request) {
    return populateSpecificInfosFromNewRequest(target, request);
  }

  @Override
  protected FindElementResponse populateFindElementResponseWithSpecificInfos(
      BookDocument doc, FindElementResponse target) {
    populateAbstractBookBaseResponse(doc, target);
    return target;
  }
  
  /**
   * 
   * @param src
   * @param target
   */
  private void populateAbstractBookBaseResponse(BookDocument doc, AbstractBookBaseResponse target) {
    target.setTitle(doc.getTitle());
    target.setAuthor(doc.getAuthor());
    target.setDescription(doc.getDescription());
    target.setDistributer(doc.getDistributer());
    target.setIsbn(doc.getIsbn());
    target.setNbPages(doc.getNbPages());
    target.setPublisher(doc.getPublisher());
    target.setGlobalRating(doc.getGlobalRating());
  }

}
