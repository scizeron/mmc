package com.stfciz.mmc.core.music.domain;

import java.util.Date;

/**
 * 
 * @author stfciz
 *
 */
public class UpdatePrice {

  private int  euroPrice;

  private Date date;

  /**
   * @return the euroPrice
   */
  public int getEuroPrice() {
    return this.euroPrice;
  }

  /**
   * @param euroPrice
   *          the euroPrice to set
   */
  public void setEuroPrice(int euroPrice) {
    this.euroPrice = euroPrice;
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
}