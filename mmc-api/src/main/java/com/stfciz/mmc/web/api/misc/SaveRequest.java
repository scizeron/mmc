package com.stfciz.mmc.web.api.misc;

import com.stfciz.mmc.web.api.AbstractSaveRequest;

/**
 * 
 * @author stfciz
 *
 * 16 juin 2015
 */
public class SaveRequest extends AbstractSaveRequest {

  /** uses the Goldmine Standard code **/
  private Integer globalRating;

  public Integer getGlobalRating() {
    return globalRating;
  }

  public void setGlobalRating(Integer globalRating) {
    this.globalRating = globalRating;
  }
}
