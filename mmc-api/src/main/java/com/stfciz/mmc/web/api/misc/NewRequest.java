package com.stfciz.mmc.web.api.misc;

import com.stfciz.mmc.web.api.AbstractNewRequest;

/**
 * 
 * @author stfciz
 *
 * 16 juin 2015
 */
public class NewRequest extends AbstractNewRequest {

  private String title;
  
  private String description;

  /** uses the Goldmine Standard code **/
  private Integer globalRating;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getGlobalRating() {
    return globalRating;
  }

  public void setGlobalRating(Integer globalRating) {
    this.globalRating = globalRating;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
