package com.stfciz.mmc.web.api.music;

import java.util.ArrayList;
import java.util.List;

import com.stfciz.mmc.web.api.UpdatePrice;

/**
 * 
 * @author stfciz
 *
 */
public class UpdateRequest extends NewRequest {
  
  private String            id;
  
  private List<UpdatePrice> prices;

  /**
   * 
   * @return
   */
  public String getId() {
    return this.id;
  }

  /**
   * 
   * @param id
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * 
   * @return
   */
  public List<UpdatePrice> getPrices() {
    if (this.prices == null) {
      this.prices = new ArrayList<>();
    }
    return this.prices;
  }

  /**
   * 
   * @param prices
   */
  public void setPrices(List<UpdatePrice> prices) {
    this.prices = prices;
  }
 
}
