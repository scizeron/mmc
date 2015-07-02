package com.stfciz.mmc.core.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.stfciz.mmc.core.photo.domain.ImageComparator;
import com.stfciz.mmc.core.photo.domain.PhotoDocument;

/**
 * 
 * @author Bellevue
 *
 */
public abstract class AbstractDocument {

  @Id
  private String            id;
  
  private String            title;
  
  private Date              modified;
  
  private Purchase          purchase;

  private String            comment;
  
  /** CODE ISO **/
  private String            origin;
  
  /** reEdition  **/
  private boolean           reEdition;

  /** year  **/
  private Integer           issue;
  
  /** limited edition number **/
  private Integer           pubNum;

  /** limited edition total **/
  private Integer           pubTotal;
  
  private boolean           promo;
  
  /** photos **/
  private List<PhotoDocument> photos;
  
  /** updated prices  */
  private List<UpdatePrice> prices;
  
  private Integer           mostUpdatedPrice;
      
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getModified() {
    return modified;
  }

  public void setModified(Date modified) {
    this.modified = modified;
  }

  public Purchase getPurchase() {
    return purchase;
  }

  public void setPurchase(Purchase purchase) {
    this.purchase = purchase;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
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
  
  /**
   * @param photos
   *          the photos to set
   */
  public void setPhotos(List<PhotoDocument> photos) {
    if (photos != null) {
      Collections.sort(photos, ImageComparator.get());
      this.photos = photos;
    }
  }
  
  /**
   * @return the photos
   */
  public List<PhotoDocument> getPhotos() {
    if (this.photos == null) {
      this.photos = new ArrayList<>();
    }
    return this.photos;
  }
  
  /**
   * @return the prices
   */
  public List<UpdatePrice> getPrices() {
    if (this.prices == null) {
      this.prices = new ArrayList<>();
    }
    return this.prices;
  }

  /**
   * @param prices
   *          the prices to set
   */
  public void setPrices(List<UpdatePrice> prices) {
    this.prices = prices;
  }

  /**
   * 
   * @return
   */
  public Integer getMostUpdatedPrice() {
    return mostUpdatedPrice;
  }

  /**
   * 
   * @param mostUpdatedPrice
   */
  public void setMostUpdatedPrice(Integer mostUpdatedPrice) {
    this.mostUpdatedPrice = mostUpdatedPrice;
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
}


