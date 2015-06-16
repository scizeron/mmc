package com.stfciz.mmc.web.api.book;
/**
 * 
 * @author stfciz
 *
 * 16 juin 2015
 */
public class UpdateRequest extends NewRequest {

  private String            id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
