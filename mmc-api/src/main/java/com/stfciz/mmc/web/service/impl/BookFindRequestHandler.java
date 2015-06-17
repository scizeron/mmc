package com.stfciz.mmc.web.service.impl;

import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.stfciz.mmc.web.service.FindRequestHandler;

@Component("bookFindRequestHandler")
public class BookFindRequestHandler implements FindRequestHandler {

  @Override
  public Sort getSort(String index) {
    return new Sort(new Sort.Order(Sort.Direction.ASC, "title"));
  }
  
  @Override
  public void customizeQueryStringQueryBuilder(
      QueryStringQueryBuilder queryBuilder) {
    queryBuilder.field("title");
  }
}
