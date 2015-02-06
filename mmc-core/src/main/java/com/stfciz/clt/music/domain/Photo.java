package com.stfciz.clt.music.domain;

/**
 * 
 * @author stcizero
 *
 */
public class Photo {

  private String id;
  
  private Integer order;
  
  private String thumbUrl;
  
  private String mediumUrl;
  
  private String originalUrl;
  
  /**
   * 
   * @param id
   */
  public Photo(String id) {
   this.id = id; 
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getOrder() {
    return order;
  }

  public void setOrder(Integer order) {
    this.order = order;
  }

  public String getThumbUrl() {
    return thumbUrl;
  }

  public void setThumbUrl(String thumbUrl) {
    this.thumbUrl = thumbUrl;
  }

  public String getMediumUrl() {
    return mediumUrl;
  }

  public void setMediumUrl(String mediumUrl) {
    this.mediumUrl = mediumUrl;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public void setOriginalUrl(String originalUrl) {
    this.originalUrl = originalUrl;
  }

}
