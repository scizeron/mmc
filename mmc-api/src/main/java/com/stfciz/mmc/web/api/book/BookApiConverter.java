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
  public GetResponse convertToGetResponse(BookDocument doc) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BookDocument convertNewRequestContent(NewRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public FindElementResponse convertToFindDocument(BookDocument doc) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BookDocument convertUpdateRequestContent(UpdateRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public FindResponse newFindResponse() {
    return new FindResponse();
  }

}
