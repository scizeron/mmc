package com.stfciz.mmc.web.service.impl;

import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.stfciz.mmc.web.service.FindRequestHandler;

/**
 * 
 * @author stfciz
 *
 * 29 juin 2015
 */

@Component
public class FindRequestHandlerImpl implements FindRequestHandler {

  private static final String ALL_FIELD = "_all";

  private static final String MUSIC_INDEX = "music";

  private static final Sort MUSIC_SORT = new Sort(new Sort.Order(Sort.Direction.ASC, "artist"),
      new Sort.Order(Sort.Direction.ASC, "title"));
  
  private static final Sort DEFAULT_SORT = new Sort(new Sort.Order(Sort.Direction.ASC, "title"));
  
  @Override
  public Sort getSort(String index) {
    return (MUSIC_INDEX.equals(index)) ? MUSIC_SORT : DEFAULT_SORT;
  }
  
  @Override
  public void customizeQueryStringQueryBuilder(
      QueryStringQueryBuilder queryBuilder) {
    queryBuilder.field(ALL_FIELD);
  }
  
}
