package com.stfciz.mmc.web.service;

import com.stfciz.mmc.core.domain.DocumentType;

/**
 * 
 * @author stfciz
 *
 * 8 juin 2015
 */
public interface AggregationService {

  /**
   * 
   * @param type
   * @return
   */
  Long getSumOfPurchases(DocumentType type);

  /**
   * 
   * @param type
   * @return
   */
  Long getSumOfUpatedPrices(DocumentType type);
  
  /**
   * 
   * @param type
   * @return
   */
  Long getMaxPurchase(DocumentType type);
  
}