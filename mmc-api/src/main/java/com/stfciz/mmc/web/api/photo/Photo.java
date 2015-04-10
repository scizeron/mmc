package com.stfciz.mmc.web.api.photo;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author stfciz
 *
 */
@JsonInclude(Include.NON_NULL)
public class Photo {
  
  /**
   * 
   * @author stfciz
   *
   */
  public static class PhotoDetails {
    
    private String type; 
    
    private String url;
    
    private int width;
    
    private int height;

    public String getType() {
      return this.type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getUrl() {
      return this.url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public int getWidth() {
      return this.width;
    }

    public void setWidth(int width) {
      this.width = width;
    }

    public int getHeight() {
      return this.height;
    }

    public void setHeight(int height) {
      this.height = height;
    }
  }
  
  private String id;
  
  private Map<String,PhotoDetails> details = new HashMap<>();
  
  private String docId;
  
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
   * 
   * @return
   */
  public Map<String, PhotoDetails> getDetails() {
    return this.details;
  }
  
  /**
   * 
   * @param details
   */
  public void setDetails(Map<String, PhotoDetails> details) {
    this.details = details;
  }
  
  /**
   * 
   * @return
   */
  public String getDocId() {
    return docId;
  }
  
  /**
   * 
   * @param docId
   */
  public void setDocId(String docId) {
    this.docId = docId;
  }
}
