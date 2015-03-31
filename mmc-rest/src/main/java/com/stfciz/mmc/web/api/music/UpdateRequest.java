package com.stfciz.mmc.web.api.music;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author stfciz
 *
 */
public class UpdateRequest extends NewRequest {
  
  private String            id;
  
  private List<UpdatePrice> prices;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<UpdatePrice> getPrices() {
    if (this.prices == null) {
      this.prices = new ArrayList<>();
    }
    return this.prices;
  }

  public void setPrices(List<UpdatePrice> prices) {
    this.prices = prices;
  }
 
}
