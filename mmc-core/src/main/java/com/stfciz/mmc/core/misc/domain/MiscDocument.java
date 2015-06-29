package com.stfciz.mmc.core.misc.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import com.stfciz.mmc.core.domain.AbstractDocument;

/**
 * 
 * @author Bellevue
 *
 */
@Document(indexName = "misc", type = "ms")
public class MiscDocument extends AbstractDocument {

  private String title;
  
  private String description;

  /** uses the Goldmine Standard code **/
  private Integer globalRating;

  public Integer getGlobalRating() {
    return globalRating;
  }

  public void setGlobalRating(Integer globalRating) {
    this.globalRating = globalRating;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
