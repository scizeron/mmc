package com.stfciz.mmc.web.api.search;

import org.springframework.beans.BeanUtils;

import com.stfciz.mmc.web.api.AbstractBaseResponse;

/**
 * 
 * @author stfciz
 *
 * 29 juin 2015
 */
public class SearchElementResponse extends AbstractBaseResponse {

  private String type;
  
  /**
   * 
   */
  public SearchElementResponse() {
    
  }
  
  /**
   * 
   * @param response
   */
  public SearchElementResponse(AbstractBaseResponse response, String type) {
    BeanUtils.copyProperties(response, this);
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setSrc(AbstractBaseResponse src) {
    BeanUtils.copyProperties(src, this);
  }
}
