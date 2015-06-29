package com.stfciz.mmc.web.service.impl;

import org.elasticsearch.index.query.QueryStringQueryBuilder;

import com.stfciz.mmc.web.service.FindRequestHandler;

/**
 * 
 * @author stfciz
 *
 * 18 juin 2015
 */
public abstract class AbstractFindRequestHandler implements FindRequestHandler {

  @Override
  public void customizeQueryStringQueryBuilder(
      QueryStringQueryBuilder queryBuilder) {
    queryBuilder.field("_all");
  }

}
