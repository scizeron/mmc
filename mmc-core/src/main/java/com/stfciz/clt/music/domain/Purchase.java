package com.stfciz.clt.music.domain;

import java.util.Date;
/**
 * 
 * @author ByTel
 *
 */
public class Purchase {
  
  private int    price;
  
  private Date   date;
  
  private String vendor;
  
  private String context;

  /**
   * @return the price
   */
  public int getPrice() {
    return this.price;
  }

  /**
   * @param price
   *          the price to set
   */
  public void setPrice(int price) {
    this.price = price;
  }

  /**
   * @return the date
   */
  public Date getDate() {
    return this.date;
  }

  /**
   * @param date
   *          the date to set
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * @return the vendor
   */
  public String getVendor() {
    return this.vendor;
  }

  /**
   * @param vendor
   *          the vendor to set
   */
  public void setVendor(String vendor) {
    this.vendor = vendor;
  }

  /**
   * @return the context
   */
  public String getContext() {
    return this.context;
  }

  /**
   * @param context the context to set
   */
  public void setContext(String context) {
    this.context = context;
  }
}
