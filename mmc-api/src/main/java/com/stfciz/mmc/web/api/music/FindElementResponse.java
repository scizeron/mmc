package com.stfciz.mmc.web.api.music;


/**
 * 
 * @author stfciz
 *
 */
public class FindElementResponse extends AbstractBaseResponse {

  private String thumbImageUrl;

  public String getThumbImageUrl() {
    return this.thumbImageUrl;
  }

  public void setThumbImageUrl(String thumbImageUrl) {
    this.thumbImageUrl = thumbImageUrl;
  }
}