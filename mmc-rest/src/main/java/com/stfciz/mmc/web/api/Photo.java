package com.stfciz.mmc.web.api;

import java.util.HashMap;
import java.util.Map;

public class Photo {
  private String id;
  private Map<String,String> urls = new HashMap<>();
  /**
   * @return the id
   */
  public String getId() {
    return this.id;
  }
  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }
  
  /**
   * @return the url
   */
  public  Map<String,String> getUrls() {
    return this.urls;
  }
  
  /**
   * @param url the url to set
   */
  public void putUrl(String type, String url) {
    this.urls.put(type, url);
  }
}
