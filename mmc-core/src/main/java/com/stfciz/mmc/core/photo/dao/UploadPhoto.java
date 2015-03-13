package com.stfciz.mmc.core.photo.dao;

import java.util.ArrayList;
import java.util.List;

import com.stfciz.mmc.core.photo.domain.Tag;

/**
 * 
 * @author stfciz
 *
 */
public class UploadPhoto {

  private String       filename;
  
  private byte[]       content;
  
  private String       photoSetId;

  private String       title;
  
  private boolean      async;
  
  private List<Tag>    tags;

  /**
   * @return the filename
   */
  public String getFilename() {
    return this.filename;
  }

  /**
   * @param filename
   *          the filename to set
   */
  public void setFilename(String filename) {
    this.filename = filename;
  }

  /**
   * @return the photoSetId
   */
  public String getPhotoSetId() {
    return this.photoSetId;
  }

  /**
   * @param photoSetId
   *          the photoSetId to set
   */
  public void setPhotoSetId(String photoSetId) {
    this.photoSetId = photoSetId;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * @param title
   *          the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the content
   */
  public byte[] getContent() {
    return this.content;
  }

  /**
   * @param content
   *          the content to set
   */
  public void setContent(byte[] content) {
    this.content = content;
  }

  /**
   * @return the async
   */
  public boolean isAsync() {
    return this.async;
  }

  /**
   * @param async
   *          the async to set
   */
  public void setAsync(boolean async) {
    this.async = async;
  }

  /**
   * @return the tags
   */
  public List<Tag> getTags() {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    return this.tags;
  }

  /**
   * @param tags the tags to set
   */
  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }
}
