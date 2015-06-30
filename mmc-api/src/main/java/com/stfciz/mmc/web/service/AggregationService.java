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
   * @param indices
   * @param types
   * @return
   */
  Long getSumOfPurchases(String [] indices, String [] types);

  /**
   * 
   * @param indices
   * @param types
   * @return
   */
  Long getSumOfUpatedPrices(String [] indices, String [] types);
  
  /**
   * 
   * @param indices
   * @param types
   * @return
   */
  Long getMaxPurchase(String [] indices, String [] types);
  
}