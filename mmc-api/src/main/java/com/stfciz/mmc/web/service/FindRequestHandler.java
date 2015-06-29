package com.stfciz.mmc.web.service;

import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.data.domain.Sort;

/**
 * 
 * @author stfciz
 *
 * 17 juin 2015
 */
public interface FindRequestHandler {

  /**
   * 
   * @param index
   * @return
   */
  Sort getSort(String index);
  
  /**
   * 
   * @param queryBuilder
   */
  void customizeQueryStringQueryBuilder(QueryStringQueryBuilder queryBuilder);
}
