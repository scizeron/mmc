package com.stfciz.clt.web.api.music;

/**
 * 
 * @author stfciz
 *
 */
public class SaveRequest extends NewRequest {
  
  private String            id;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }
 
}
