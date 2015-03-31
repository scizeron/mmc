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

  private String obiColor;

  private String obiPos;

  private String comment;

  private Integer purchaseMonth;
  
  private List<UpdatePrice> prices;
  
  public Integer getPurchaseMonth() {
    return purchaseMonth;
  }

  public void setPurchaseMonth(Integer purchaseMonth) {
    this.purchaseMonth = purchaseMonth;
  }

  public Integer getPurchaseYear() {
    return purchaseYear;
  }

  public void setPurchaseYear(Integer purchaseYear) {
    this.purchaseYear = purchaseYear;
  }

  private Integer purchaseYear;

  private Integer purchasePrice;

  private String purchaseVendor;

  private String purchaseContext;

  private List<Photo> images;

  public String getObiColor() {
    return this.obiColor;
  }

  public void setObiColor(String obiColor) {
    this.obiColor = obiColor;
  }

  public String getObiPos() {
    return this.obiPos;
  }

  public void setObiPos(String obiPos) {
    this.obiPos = obiPos;
  }

  public String getComment() {
    return this.comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Integer getPurchasePrice() {
    return this.purchasePrice;
  }

  public void setPurchasePrice(Integer purchasePrice) {
    this.purchasePrice = purchasePrice;
  }

  public String getPurchaseVendor() {
    return this.purchaseVendor;
  }

  public void setPurchaseVendor(String purchaseVendor) {
    this.purchaseVendor = purchaseVendor;
  }

  public String getPurchaseContext() {
    return this.purchaseContext;
  }

  public void setPurchaseContext(String purchaseContext) {
    this.purchaseContext = purchaseContext;
  }

  public List<Photo> getImages() {
    return this.images;
  }

  public void setImages(List<Photo> images) {
    this.images = images;
  }

  public List<UpdatePrice> getPrices() {
    if (this.prices == null) {
     this.prices = new ArrayList<>();
    }
    return prices;
  }

  public void setPrices(List<UpdatePrice> prices) {
    this.prices = prices;
  }
}
