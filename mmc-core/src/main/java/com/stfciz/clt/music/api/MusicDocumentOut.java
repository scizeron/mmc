package com.stfciz.clt.music.api;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.stfciz.clt.music.domain.Image;
import com.stfciz.clt.music.domain.ImageComparator;
import com.stfciz.clt.music.domain.MusicDocument;

/**
 * 
 * @author ByTel
 * 
 */
@JsonInclude(Include.NON_NULL)
public class MusicDocumentOut extends MusicDocument {

  private String  thumbImageUrl;

  private boolean sorted = false;

  /**
   * @return the thumbImageUrl
   */
  public String getThumbImageUrl() {
    return this.thumbImageUrl;
  }

  /**
   * @param thumbImageUrl
   *          the thumbImageUrl to set
   */
  public void setThumbImageUrl(String thumbImageUrl) {
    this.thumbImageUrl = thumbImageUrl;
  }

  /**
   * 
   * @return
   */
  public List<Image> getSortedImages() {
    List<Image> imgs = getImages();
    if (this.sorted) {
      return imgs;
    }

    if (imgs != null) {
      Collections.sort(imgs, ImageComparator.get());
      this.sorted = true;
    }
    return imgs;
  }

  /**
   * 
   * @return
   */
  public String getMainImageUrl() {
    List<Image> sortedImages = getSortedImages();
    if (sortedImages != null && sortedImages.size() > 0) {
      return sortedImages.get(0).getUrl();
    }
    return null;
  }
}