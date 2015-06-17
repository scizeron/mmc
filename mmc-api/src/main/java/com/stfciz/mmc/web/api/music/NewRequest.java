package com.stfciz.mmc.web.api.music;

import java.util.ArrayList;
import java.util.List;

import com.stfciz.mmc.web.api.AbstractNewRequest;


/**
 * 
 * @author ByTel
 *
 */
public class NewRequest extends AbstractNewRequest {
  
  private String            title;
  
  private String            artist;

  private boolean           promo;
  
  private String            serialNumber;

  private String            mainType;
  
  private String            vinylColor;
  
  private String            obiColor;

  private String            obiPos;
  
  private Integer           nbType;

  private String            recordCompany;

  private String            label;
  
  private Integer           sleeveGrade;
  
  private Integer           recordGrade;
  
  private List<SideMatrix>  sideMatrixes;
  
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

  public String getSerialNumber() {
    return this.serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
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

  public String getVinylColor() {
    return vinylColor;
  }

  public void setVinylColor(String vinylColor) {
    this.vinylColor = vinylColor;
  }
  
  /**
   * 
   * @return
   */
  public List<SideMatrix> getSideMatrixes() {
    if (this.sideMatrixes == null) {
      this.sideMatrixes = new ArrayList<>();
    }
    return this.sideMatrixes;
  }
  
  /**
   * 
   * @param sidesMatrix
   */
  public void setSideMatrixes(List<SideMatrix> sideMatrixes) {
    this.sideMatrixes = sideMatrixes;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
