package com.stfciz.mmc.web.api.misc;

import com.stfciz.mmc.web.api.AbstractBaseResponse;

/**
 * 
 * @author stfciz
 *
 * 29 juin 2015
 */
public abstract class AbstractMiscBaseResponse extends AbstractBaseResponse {
 
  private String title;
  
  private String description;

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
