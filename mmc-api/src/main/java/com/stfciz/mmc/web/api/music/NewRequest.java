package com.stfciz.mmc.web.api.music;


/**
 * 
 * @author ByTel
 *
 */
public class NewRequest {
  
  private String            title;

  private String            artist;

  private boolean           promo;
  
  private String            origin;

  private String            serialNumber;

  private Boolean           reEdition;

  private Integer           issue;

  private String            mainType;
  
  private String            vinylColor;
  
  private String            obiColor;

  private String            obiPos;
  
  private Integer           nbType;

  private Integer           pubNum;

  private Integer           pubTotal;

  private String            recordCompany;

  private String            label;
  
  private Integer           sleeveGrade;
  
  private Integer           recordGrade;

  private String            comment;
  
  private Integer           purchaseMonth;
  
  private Integer           purchaseYear;
 
  private Integer           purchasePrice;
  
  private String            purchaseVendor;
  
  private String            purchaseContext;
  
  public Boolean getReEdition() {
    return reEdition;
  }

  public void setReEdition(Boolean reEdition) {
    this.reEdition = reEdition;
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

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getArtist() {
    return this.artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public boolean isPromo() {
    return this.promo;
  }

  public void setPromo(boolean promo) {
    this.promo = promo;
  }

  public String getOrigin() {
    return this.origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public String getSerialNumber() {
    return this.serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }
 
  public Integer getIssue() {
    return this.issue;
  }

  public void setIssue(Integer issue) {
    this.issue = issue;
  }

  public String getMainType() {
    return this.mainType;
  }

  public void setMainType(String mainType) {
    this.mainType = mainType;
  }

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

  public Integer getNbType() {
    return this.nbType;
  }

  public void setNbType(Integer nbType) {
    this.nbType = nbType;
  }

  public Integer getPubNum() {
    return this.pubNum;
  }

  public void setPubNum(Integer pubNum) {
    this.pubNum = pubNum;
  }

  public Integer getPubTotal() {
    return this.pubTotal;
  }

  public void setPubTotal(Integer pubTotal) {
    this.pubTotal = pubTotal;
  }

  public String getRecordCompany() {
    return this.recordCompany;
  }

  public void setRecordCompany(String recordCompany) {
    this.recordCompany = recordCompany;
  }

  public String getLabel() {
    return this.label;
  }

  public void setLabel(String label) {
    this.label = label;
  }
  
  public Integer getSleeveGrade() {
    return this.sleeveGrade;
  }

  public void setSleeveGrade(Integer sleeveGrade) {
    this.sleeveGrade = sleeveGrade;
  }

  public Integer getRecordGrade() {
    return this.recordGrade;
  }

  public void setRecordGrade(Integer recordGrade) {
    this.recordGrade = recordGrade;
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

  public String getVinylColor() {
    return vinylColor;
  }

  public void setVinylColor(String vinylColor) {
    this.vinylColor = vinylColor;
  }
}
