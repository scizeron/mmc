package com.stfciz.mmc.core.music.domain;


/**
 * 
 * @author stfciz
 *
 */
public class Purchase {
  
  private Integer price;
  
  private Integer month;
  
  private Integer year;

  private String vendor;
  
  private String context;

  /**
   * 
   * @return
   */
  public Integer getPrice() {
    return price;
  }

  /**
   * 
   * @param price
   */
  public void setPrice(Integer price) {
    this.price = price;
  }

  /**
   * 
   * @return
   */
  public Integer getMonth() {
    return month;
  }

  /**
   * 
   * @param month
   */
  public void setMonth(Integer month) {
    this.month = month;
  }

  /**
   * 
   * @return
   */
  public Integer getYear() {
    return year;
  }

  /**
   * 
   * @param year
   */
  public void setYear(Integer year) {
    this.year = year;
  }

  /**
   * 
   * @return
   */
  public String getVendor() {
    return vendor;
  }

  /**
   * 
   * @param vendor
   */
  public void setVendor(String vendor) {
    this.vendor = vendor;
  }
  

  /**
   * 
   * @return
   */
  public String getContext() {
    return context;
  }

  /**
   * 
   * @param context
   */
  public void setContext(String context) {
    this.context = context;
  }

}
