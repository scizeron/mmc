package com.stfciz.mmc.web.service;

/**
 * 
 * @author stfciz
 *
 * 8 juin 2015
 */
public interface AggregationService {

  /**
   * 
   * @return
   */
  Long getSumOfPurchases(String type);

  /**
   * 
   * @param type
   * @return
   */
  Long getSumOfUpatedPrices(String type);
  
  /**
   * 
   * @param type
   * @return
   */
  Long getMaxPurchase(String type);
  
}