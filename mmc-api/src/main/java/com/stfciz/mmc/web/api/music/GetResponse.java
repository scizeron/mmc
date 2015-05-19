package com.stfciz.mmc.web.api.music;

import java.util.ArrayList;
import java.util.List;

import com.stfciz.mmc.web.api.photo.Photo;

/**
 * 
 * @author stfciz
 *
 */
public class GetResponse extends AbstractBaseResponse {

  private String comment;

  private Integer purchaseMonth;

  private Integer purchaseYear;

  private Integer purchasePrice;

  private String purchaseVendor;

  private String purchaseContext;

  private List<Photo> images;

  private List<UpdatePrice> prices;
  
  private List<SideMatrix>  sideMatrixes;

  /**
   * 
   * @return
   */
  public Integer getPurchaseMonth() {
    return purchaseMonth;
  }

  /**
   * 
   * @param purchaseMonth
   */
  public void setPurchaseMonth(Integer purchaseMonth) {
    this.purchaseMonth = purchaseMonth;
  }

  /**
   * 
   * @return
   */
  public Integer getPurchaseYear() {
    return purchaseYear;
  }

  /**
   * 
   * @param purchaseYear
   */
  public void setPurchaseYear(Integer purchaseYear) {
    this.purchaseYear = purchaseYear;
  }

  /**
   * 
   * @return
   */
  public String getComment() {
    return this.comment;
  }

  /**
   * 
   * @param comment
   */
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * 
   * @return
   */
  public Integer getPurchasePrice() {
    return this.purchasePrice;
  }

  /**
   * 
   * @param purchasePrice
   */
  public void setPurchasePrice(Integer purchasePrice) {
    this.purchasePrice = purchasePrice;
  }

  /**
   * 
   * @return
   */
  public String getPurchaseVendor() {
    return this.purchaseVendor;
  }

  /**
   * 
   * @param purchaseVendor
   */
  public void setPurchaseVendor(String purchaseVendor) {
    this.purchaseVendor = purchaseVendor;
  }

  /**
   * 
   * @return
   */
  public String getPurchaseContext() {
    return this.purchaseContext;
  }

  /**
   * 
   * @param purchaseContext
   */
  public void setPurchaseContext(String purchaseContext) {
    this.purchaseContext = purchaseContext;
  }

  /**
   * 
   * @return
   */
  public List<Photo> getImages() {
    return this.images;
  }

  /**
   * 
   * @param images
   */
  public void setImages(List<Photo> images) {
    this.images = images;
  }

  /**
   * 
   * @return
   */
  public List<UpdatePrice> getPrices() {
    if (this.prices == null) {
      this.prices = new ArrayList<>();
    }
    return prices;
  }

  /**
   * 
   * @param prices
   */
  public void setPrices(List<UpdatePrice> prices) {
    this.prices = prices;
  }
  
  /**
   * 
   * @return
   */
  public List<SideMatrix> getSideMatrixes() {
    if (this.sideMatrixes == null) {
      this.sideMatrixes = new ArrayList<>();
    }
    return this.sideMatrixes;
  }
  
  /**
   * 
   * @param sidesMatrix
   */
  public void setSideMatrixes(List<SideMatrix> sideMatrixes) {
    this.sideMatrixes = sideMatrixes;
  }
}
