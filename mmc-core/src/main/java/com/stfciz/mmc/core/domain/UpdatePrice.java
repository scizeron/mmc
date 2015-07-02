package com.stfciz.mmc.core.domain;


/**
 * 
 * @author stfciz
 *
 */
public class UpdatePrice {

  private Integer price;
  
  private Integer month;
  
  private Integer year;
  
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
}
