package com.stfciz.mmc.web.api.music;

/**
 * 
 * @author stfciz
 *
 */
public class UpdateRequest extends NewRequest {
  
  private String            id;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }
 
}
