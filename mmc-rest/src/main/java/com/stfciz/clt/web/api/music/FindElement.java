package com.stfciz.clt.web.api.music;

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
}