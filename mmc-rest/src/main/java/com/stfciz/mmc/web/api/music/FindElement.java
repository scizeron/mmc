package com.stfciz.mmc.web.api.music;

import java.util.Date;

/**
 * 
 * @author stfciz
 *
 */
public class FindElement {
  
  private String thumbImageUrl;
  
  private String id;
  
  private String title;
  
  private String artist;
  
  private Integer edition;
  
  private String mainType;

  private String nbType;
  
  private String origin;

  private Date modified;
  
  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
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

  public Integer getEdition() {
    return this.edition;
  }

  public void setEdition(Integer edition) {
    this.edition = edition;
  }

  public String getThumbImageUrl() {
    return this.thumbImageUrl;
  }

  public void setThumbImageUrl(String thumbImageUrl) {
    this.thumbImageUrl = thumbImageUrl;
  }

  public String getMainType() {
    return this.mainType;
  }

  public void setMainType(String mainType) {
    this.mainType = mainType;
  }

  public String getNbType() {
    return this.nbType;
  }

  public void setNbType(String nbType) {
    this.nbType = nbType;
  }

  public String getOrigin() {
    return this.origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public Date getModified() {
    return this.modified;
  }

  public void setModified(Date modified) {
    this.modified = modified;
  }
}