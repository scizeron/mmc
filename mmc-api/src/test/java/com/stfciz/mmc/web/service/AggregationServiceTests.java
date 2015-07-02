package com.stfciz.mmc.web.service;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.stfciz.mmc.core.domain.DocumentType;
import com.stfciz.mmc.web.AbstractWebApplicationTests;

/**
 * 
 * @author stfciz
 *
 * 8 juin 2015
 */
public class AggregationServiceTests extends AbstractWebApplicationTests {

  @Autowired
  private AggregationService service;
  
  /**
   * 
   */
//  @Test public void getSumOfPurchases() {
//    Assert.assertThat(this.service.getSumOfPurchases(DocumentType.MUSIC), CoreMatchers.is(430L));
//  }
//
//  @Test public void getSumOfUpatedPrices() {
//    Assert.assertThat(this.service.getSumOfUpatedPrices(DocumentType.MUSIC), CoreMatchers.is(595L));
//  }
  
  @Test public void getMaxPurchase() {
    Assert.assertThat(this.service.getMaxPurchase(DocumentType.MUSIC), CoreMatchers.is(260L));
  }
  

}
