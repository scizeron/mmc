package com.stfciz.mmc.web.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.stfciz.mmc.web.api.photo.Photo;

@JsonInclude(Include.NON_NULL)
public class FindItemResponse {

  private String id;
  
  private String type;
  
  private String title;

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
  
  private String description;
  
  /** music **/
  private String artist;
  
  private String serialNumber;
  
  private String mainType;
  
  private String vinylColor;
  
  private String obiColor;
  
  private String obiPos; 
  
  private Integer nbType;
  
  private String recordCompany;
    
  private String label;
  
  private Integer sleeveGrade;
  
  private Integer recordGrade;
    
  /** book & misc **/

  private String author;

  private Integer globalRating;

  private String publisher;

  private String distributer;

  private String isbn;

  private Integer nbPages;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public String getDistributer() {
    return distributer;
  }

  public void setDistributer(String distributer) {
    this.distributer = distributer;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public Integer getNbPages() {
    return nbPages;
  }

  public void setNbPages(Integer nbPages) {
    this.nbPages = nbPages;
  }
  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getMainType() {
    return mainType;
  }

  public void setMainType(String mainType) {
    this.mainType = mainType;
  }

  public String getVinylColor() {
    return vinylColor;
  }

  public void setVinylColor(String vinylColor) {
    this.vinylColor = vinylColor;
  }

  public String getObiColor() {
    return obiColor;
  }

  public void setObiColor(String obiColor) {
    this.obiColor = obiColor;
  }

  public String getObiPos() {
    return obiPos;
  }

  public void setObiPos(String obiPos) {
    this.obiPos = obiPos;
  }

  public Integer getNbType() {
    return nbType;
  }

  public void setNbType(Integer nbType) {
    this.nbType = nbType;
  }

  public String getRecordCompany() {
    return recordCompany;
  }

  public void setRecordCompany(String recordCompany) {
    this.recordCompany = recordCompany;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Integer getSleeveGrade() {
    return sleeveGrade;
  }

  public void setSleeveGrade(Integer sleeveGrade) {
    this.sleeveGrade = sleeveGrade;
  }

  public Integer getRecordGrade() {
    return recordGrade;
  }

  public void setRecordGrade(Integer recordGrade) {
    this.recordGrade = recordGrade;
  }

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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
