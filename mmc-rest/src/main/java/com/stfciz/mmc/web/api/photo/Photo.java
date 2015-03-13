package com.stfciz.mmc.web.api.photo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author stfciz
 *
 */
public class Photo {
  
  private String id;
  
  private Map<String,String> urls = new HashMap<>();
  
  private List<String> musicDocIds;
  
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
  
  /**
   * 
   * @return
   */
  public List<String> getMusicDocIds() {
    if (this.musicDocIds == null) {
      this.musicDocIds = new ArrayList<>();
    }
    return this.musicDocIds;
  }
  
  /**
   * 
   * @param musicDocIds
   */
  public void setMusicDocIds(List<String> musicDocIds) {
    this.musicDocIds = musicDocIds;
  }
}
