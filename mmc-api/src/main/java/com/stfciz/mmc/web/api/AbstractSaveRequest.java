package com.stfciz.mmc.web.api;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author stfciz
 *
 * 16 juin 2015
 */
public abstract class AbstractSaveRequest {

  private String            description;
  
  private String            comment;
  
  private Integer           purchaseMonth;
  
  private Integer           purchaseYear;
 
  private Integer           purchasePrice;
  
  private String            purchaseVendor;
  
  private String            purchaseContext;
  
  private Integer           issue;
  
  private Boolean           reEdition;
  
  private String            origin;
  
  private Integer           pubNum;

  private Integer           pubTotal;
  
  private boolean           promo;
 
  private String            id;
  
  private String            title;

  private List<UpdatePrice> prices;
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
  
  /**
   * 
   * @return
   */
  public List<UpdatePrice> getPrices() {
    if (this.prices == null) {
      this.prices = new ArrayList<>();
    }
    return this.prices;
  }

  /**
   * 
   * @param prices
   */
  public void setPrices(List<UpdatePrice> prices) {
    this.prices = prices;
  }
  
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

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

  public Integer getPurchasePrice() {
    return purchasePrice;
  }

  public void setPurchasePrice(Integer purchasePrice) {
    this.purchasePrice = purchasePrice;
  }

  public String getPurchaseVendor() {
    return purchaseVendor;
  }

  public void setPurchaseVendor(String purchaseVendor) {
    this.purchaseVendor = purchaseVendor;
  }

  public String getPurchaseContext() {
    return purchaseContext;
  }

  public void setPurchaseContext(String purchaseContext) {
    this.purchaseContext = purchaseContext;
  }

  public Integer getIssue() {
    return issue;
  }

  public void setIssue(Integer issue) {
    this.issue = issue;
  }

  public Boolean getReEdition() {
    return reEdition;
  }

  public void setReEdition(Boolean reEdition) {
    this.reEdition = reEdition;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
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

  public boolean isPromo() {
    return promo;
  }

  public void setPromo(boolean promo) {
    this.promo = promo;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
