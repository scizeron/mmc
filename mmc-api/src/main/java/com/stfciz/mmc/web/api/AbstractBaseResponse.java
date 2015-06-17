package com.stfciz.mmc.web.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.stfciz.mmc.web.api.photo.Photo;

@JsonInclude(Include.NON_NULL)
public abstract class AbstractBaseResponse {

  private String id;

  private Date lastModified;

  private boolean promo;

  private String origin;
  
  private boolean reEdition;
  
  private Integer issue;

  private Integer pubNum;
  
  private Integer pubTotal;

  private String thumbImageUrl;
  
  private Integer purchaseMonth;

  private Integer purchaseYear;

  private Integer purchasePrice;

  private String purchaseVendor;

  private String purchaseContext;

  private String comment;
  
  private List<Photo> images;

  private List<UpdatePrice> prices;

  public Integer getPurchaseMonth() {
    return this.purchaseMonth;
  }

  public void setPurchaseMonth(Integer purchaseMonth) {
    this.purchaseMonth = purchaseMonth;
  }

  public Integer getPurchaseYear() {
    return this.purchaseYear;
  }

  public void setPurchaseYear(Integer purchaseYear) {
    this.purchaseYear = purchaseYear;
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
      this.prices = new ArrayList<UpdatePrice>();
    }
    return this.prices;
  }

  public void setPrices(List<UpdatePrice> prices) {
    this.prices = prices;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
  
  public String getThumbImageUrl() {
    return this.thumbImageUrl;
  }

  public void setThumbImageUrl(String thumbImageUrl) {
    this.thumbImageUrl = thumbImageUrl;
  }
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getLastModified() {
    return lastModified;
  }

  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

  public boolean isPromo() {
    return promo;
  }

  public void setPromo(boolean promo) {
    this.promo = promo;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public boolean isReEdition() {
    return reEdition;
  }

  public void setReEdition(boolean reEdition) {
    this.reEdition = reEdition;
  }

  public Integer getIssue() {
    return issue;
  }

  public void setIssue(Integer issue) {
    this.issue = issue;
  }

  public Integer getPubNum() {
    return pubNum;
  }

  public void setPubNum(Integer pubNum) {
    this.pubNum = pubNum;
  }

  public Integer getPubTotal() {
    return pubTotal;
  }

  public void setPubTotal(Integer pubTotal) {
    this.pubTotal = pubTotal;
  }
}
