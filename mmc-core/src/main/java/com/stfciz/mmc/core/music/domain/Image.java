package com.stfciz.mmc.core.music.domain;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author stfciz
 *
 */
public class Image {

  private String  url;

  private Integer order;

  /**
   * 
   */
  public Image() {
    /** EMPTY **/
  }

  public Image(String url, Integer order) {
    // bug dans Photo _getBaseImageUrl
    this.url = StringUtils.replace(url, "static.flickr", "staticflickr",1);
    this.order = order;
  }

  /**
   * @return the url
   */
  public String getUrl() {
    return this.url;
  }

  /**
   * @param url
   *          the url to set
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * @return the order
   */
  public Integer getOrder() {
    return this.order;
  }

  /**
   * @param order the order to set
   */
  public void setOrder(Integer order) {
    this.order = order;
  }
}
