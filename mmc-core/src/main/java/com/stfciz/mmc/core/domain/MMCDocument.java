package com.stfciz.mmc.core.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.stfciz.mmc.core.domain.music.Obi;
import com.stfciz.mmc.core.domain.music.RecordCompany;
import com.stfciz.mmc.core.domain.music.SideMatrix;
import com.stfciz.mmc.core.domain.music.Song;
import com.stfciz.mmc.core.photo.domain.ImageComparator;
import com.stfciz.mmc.core.photo.domain.PhotoDocument;

/**
 * 
 * @author stfciz
 *
 * 2 juil. 2015
 */
@Document(indexName = "music", type = "md")
public class MMCDocument {

  @Id
  private String            id;
  
  private String            type; 
  
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
  
  private String description;
  /**
   * music fields
   */
  private String            artist;

  private String            serialNumber;

  /** code : LP, EP, CD ... **/
  private String            mainType;

  /** 2, 3 CD**/
  private Integer           nbType;
    
  private String            vinylColor;
  
  private Obi               obi;

  private RecordCompany     recordCompany;

  /** uses the Goldmine Standard code **/
  private Integer           sleeveRating;

  /** uses the Goldmine Standard code **/
  private Integer           recordRating;
  
  /** EP/LP side matrix number **/
  private List<SideMatrix>  sideMatrixes;
  
  private List<Song>        songs; 
      
  /**
   * book & misc fields
   * 
   */
  private String author;

  /** uses the Goldmine Standard code **/
  private Integer globalRating;
  
  /**
   * 
   */
  private String publisher;

  /**
   * 
   */
  private String distributer;

  /**
   * 
   */
  private String isbn;

  /**
   * 
   */
  private Integer nbPages;
  
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
   * 
   * @return
   */
  public String getVinylColor() {
    return vinylColor;
  }

  /**
   * 
   * @param vinylColor
   */
  public void setVinylColor(String vinylColor) {
    this.vinylColor = vinylColor;
  }

  /**
   * 
   * @return
   */
  public List<SideMatrix> getSideMatrixs() {
    if (this.sideMatrixes == null) {
      this.sideMatrixes = new ArrayList<>();
    }
    return this.sideMatrixes;
  }
  
  /**
   * 
   * @param sidesMatrix
   */
  public void setSideMatrixs(List<SideMatrix> sideMatrixes) {
    this.sideMatrixes = sideMatrixes;
  }

  /**
   * 
   * @return
   */
  public List<Song> getSongs() {
    if (this.songs == null) {
      this.songs = new ArrayList<>();
    }
    return this.songs;
  }

  /**
   * 
   * @param songs
   */
  public void setSongs(List<Song> songs) {
    this.songs = songs;
  }
  
  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public Integer getGlobalRating() {
    return globalRating;
  }

  public void setGlobalRating(Integer globalRating) {
    this.globalRating = globalRating;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDistributer() {
    return distributer;
  }

  public void setDistributer(String distributer) {
    this.distributer = distributer;
  }

  public Integer getNbPages() {
    return nbPages;
  }

  public void setNbPages(Integer nbPages) {
    this.nbPages = nbPages;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}


