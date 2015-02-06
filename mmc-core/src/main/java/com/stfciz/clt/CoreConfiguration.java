package com.stfciz.clt;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.stfciz.clt.photo.FlickrConfiguration;

@ConfigurationProperties(prefix = "app.core")
public class CoreConfiguration {

  @NotNull
  private FlickrConfiguration flickr;

  /**
   * @return the flickr
   */
  public FlickrConfiguration getFlickr() {
    return this.flickr;
  }

  /**
   * @param flickr
   *          the flickr to set
   */
  public void setFlickr(FlickrConfiguration flickr) {
    this.flickr = flickr;
  }
}
