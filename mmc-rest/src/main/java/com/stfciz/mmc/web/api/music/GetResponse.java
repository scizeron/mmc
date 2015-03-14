package com.stfciz.mmc.web.api.music;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author stfciz
 *
 */
public class GetResponse extends AbstractBaseResponse {

  private String obiColor;

  private String obiPos;

  private String comment;

  private Date purchaseDate;

  private Integer purchasePrice;

  private String purchaseVendor;

  private String purchaseContext;

  private List<Map<String, String>> images;

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

  public Date getPurchaseDate() {
    return this.purchaseDate;
  }

  public void setPurchaseDate(Date purchaseDate) {
    this.purchaseDate = purchaseDate;
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

  public List<Map<String, String>> getImages() {
    return this.images;
  }

  public void setImages(List<Map<String, String>> images) {
    this.images = images;
  }
}
