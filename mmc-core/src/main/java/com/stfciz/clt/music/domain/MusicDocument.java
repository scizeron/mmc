package com.stfciz.clt.music.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 
 * @author ByTel
 * 
 */
@Document(indexName = "music", type = "md")

public class MusicDocument {

  @Id
  private String            id;

  private String            title;

  private String            artist;

  private boolean           promo;
  
  private String            origin;

  private String            serialNumber;

  private Integer           edition;

  private Integer           issue;

  private String            mainType;
  
  private String            obiColor;

  private String            nbType;

  private Integer           pubNum;

  private Integer           pubTotal;

  private String            recordCompany;

  private String            label;

  private String            comment;

  private List<Image>       images;

  private Purchase          purchase;

  private List<UpdatePrice> prices;
  


  /**
   * @return the id
   */
  public String getId() {
    return this.id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * @param title
   *          the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the artist
   */
  public String getArtist() {
    return this.artist;
  }

  /**
   * @param artist
   *          the artist to set
   */
  public void setArtist(String artist) {
    this.artist = artist;
  }

  /**
   * @return the origin
   */
  public String getOrigin() {
    return this.origin;
  }

  /**
   * @param origin
   *          the origin to set
   */
  public void setOrigin(String origin) {
    this.origin = origin;
  }

  /**
   * @return the serialNumber
   */
  public String getSerialNumber() {
    return this.serialNumber;
  }

  /**
   * @param serialNumber
   *          the serialNumber to set
   */
  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  /**
   * @return the edition
   */
  public Integer getEdition() {
    return this.edition;
  }

  /**
   * @param edition
   *          the edition to set
   */
  public void setEdition(Integer edition) {
    this.edition = edition;
  }

  /**
   * @return the issue
   */
  public Integer getIssue() {
    return this.issue;
  }

  /**
   * @param issue
   *          the issue to set
   */
  public void setIssue(Integer issue) {
    this.issue = issue;
  }

  /**
   * @return the mainType
   */
  public String getMainType() {
    return this.mainType;
  }

  /**
   * @param mainType
   *          the mainType to set
   */
  public void setMainType(String mainType) {
    this.mainType = mainType;
  }

  /**
   * @return the nbType
   */
  public String getNbType() {
    return this.nbType;
  }

  /**
   * @param nbType
   *          the nbType to set
   */
  public void setNbType(String nbType) {
    this.nbType = nbType;
  }

  /**
   * @return the pubNum
   */
  public Integer getPubNum() {
    return this.pubNum;
  }

  /**
   * @param pubNum
   *          the pubNum to set
   */
  public void setPubNum(Integer pubNum) {
    this.pubNum = pubNum;
  }

  /**
   * @return the pubTotal
   */
  public Integer getPubTotal() {
    return this.pubTotal;
  }

  /**
   * @param pubTotal
   *          the pubTotal to set
   */
  public void setPubTotal(Integer pubTotal) {
    this.pubTotal = pubTotal;
  }

  /**
   * @return the recordCompany
   */
  public String getRecordCompany() {
    return this.recordCompany;
  }

  /**
   * @param recordCompany
   *          the recordCompany to set
   */
  public void setRecordCompany(String recordCompany) {
    this.recordCompany = recordCompany;
  }

  /**
   * @return the label
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * @param label
   *          the label to set
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * @return the comment
   */
  public String getComment() {
    return this.comment;
  }

  /**
   * @param comment
   *          the comment to set
   */
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * @param images
   *          the images to set
   */
  public void setImages(List<Image> images) {
    this.images = images;
  }

  /**
   * @return the purchase
   */
  public Purchase getPurchase() {
    return this.purchase;
  }

  /**
   * @param purchase
   *          the purchase to set
   */
  public void setPurchase(Purchase purchase) {
    this.purchase = purchase;
  }

  /**
   * @return the prices
   */
  public List<UpdatePrice> getPrices() {
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
   * @return the images
   */
  public List<Image> getImages() {
    if (this.images == null) {
      this.images = new ArrayList<>();
    }
    return this.images;
  }

  /**
   * @return the obiColor
   */
  public String getObiColor() {
    return this.obiColor;
  }

  /**
   * @param obiColor the obiColor to set
   */
  public void setObiColor(String obiColor) {
    this.obiColor = obiColor;
  }

  public boolean isPromo() {
    return promo;
  }

  public void setPromo(boolean promo) {
    this.promo = promo;
  }  
}