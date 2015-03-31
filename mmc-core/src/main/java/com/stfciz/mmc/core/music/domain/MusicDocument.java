package com.stfciz.mmc.core.music.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.stfciz.mmc.core.music.ImageComparator;

/**
 * 
 * @author stfciz
 *
 */
@Document(indexName = "music", type = "md")
public class MusicDocument {

  @Id
  private String            id;

  private String            title;

  private String            artist;

  private boolean           promo;
  
  /** CODE ISO **/
  private String            origin;

  private String            serialNumber;

  /** year  **/
  private boolean           reEdition;



  /** year  **/
  private Integer           issue;

  /** code : LP, EP, CD ... **/
  private String            mainType;

  /** 2, 3 CD**/
  private Integer           nbType;
    
  private Obi               obi;

  /** limited edition number **/
  private Integer           pubNum;

  /** limited edition total **/
  private Integer           pubTotal;

  private RecordCompany     recordCompany;

  /** uses the Goldmine Standard code **/
  private Integer           sleeveRating;

  /** uses the Goldmine Standard code **/
  private Integer           recordRating;

  private String            comment;

  private List<PhotoMusicDocument> photos;

  private Purchase          purchase;

  private List<UpdatePrice> prices;
  
  private Date              modified;

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
  public Integer getNbType() {
    return this.nbType;
  }

  /**
   * @param nbType
   *          the nbType to set
   */
  public void setNbType(Integer nbType) {
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
  public RecordCompany getRecordCompany() {
    return this.recordCompany;
  }

  /**
   * @param recordCompany
   *          the recordCompany to set
   */
  public void setRecordCompany(RecordCompany recordCompany) {
    this.recordCompany = recordCompany;
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
   * @param photos
   *          the photos to set
   */
  public void setPhotos(List<PhotoMusicDocument> photos) {
    if (photos != null) {
      Collections.sort(photos, ImageComparator.get());
      this.photos = photos;
    }
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
   * @return the photos
   */
  public List<PhotoMusicDocument> getPhotos() {
    if (this.photos == null) {
      this.photos = new ArrayList<>();
    }
    return this.photos;
  }

  /**
   * 
   * @return
   */
  public boolean isPromo() {
    return promo;
  }

  /**
   * 
   * @param promo
   */
  public void setPromo(boolean promo) {
    this.promo = promo;
  }

  /**
   * 
   * @return
   */
  public Obi getObi() {
    return this.obi;
  }

  /**
   * 
   * @param obi
   */
  public void setObi(Obi obi) {
    this.obi = obi;
  }

  /**
   * 
   * @return
   */
  public Date getModified() {
    return this.modified;
  }

  /**
   * 
   * @param modified
   */
  public void setModified(Date modified) {
    this.modified = modified;
  }
  
  /**
   * 
   * @return
   */
  public Integer getSleeveRating() {
    return sleeveRating;
  }

  /**
   * 
   * @param sleeveRating
   */
  public void setSleeveRating(Integer sleeveRating) {
    this.sleeveRating = sleeveRating;
  }

  /**
   * 
   * @return
   */
  public Integer getRecordRating() {
    return recordRating;
  }

  /**
   * 
   * @param recordRating
   */
  public void setRecordRating(Integer recordRating) {
    this.recordRating = recordRating;
  }
  
  /**
   * *
   * @return
   */
  public boolean isReEdition() {
    return reEdition;
  }

  /**
   * 
   * @param reEdition
   */
  public void setReEdition(boolean reEdition) {
    this.reEdition = reEdition;
  }
}