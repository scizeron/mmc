package com.stfciz.mmc.core.music.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.elasticsearch.annotations.Document;

import com.stfciz.mmc.core.domain.AbstractDocument;

/**
 * 
 * @author stfciz
 *
 */
@Document(indexName = "music", type = "md")
public class MusicDocument extends AbstractDocument {

  private String            title;

  private String            artist;

  private boolean           promo;

  private String            serialNumber;

  /** code : LP, EP, CD ... **/
  private String            mainType;

  /** 2, 3 CD**/
  private Integer           nbType;
    
  private String            vinylColor;
  
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
  
  /** EP/LP side matrix number **/
  private List<SideMatrix>  sideMatrixes;
  
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
}