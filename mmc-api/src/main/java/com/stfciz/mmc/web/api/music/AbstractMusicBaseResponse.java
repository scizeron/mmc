package com.stfciz.mmc.web.api.music;

import java.util.ArrayList;
import java.util.List;

import com.stfciz.mmc.web.api.AbstractBaseResponse;

/**
 * 
 * @author stfciz
 *
 * 16 juin 2015
 */
public class AbstractMusicBaseResponse extends AbstractBaseResponse {
  
  private String title;
  
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
