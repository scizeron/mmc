package com.stfciz.clt.photo.dao;

import java.util.ArrayList;
import java.util.List;

public class UploadPhoto {

  private String       filename;
  private byte[]       content;
  private String       photoSetId;
  private String       documentId;
  private String       title;
  private boolean      async;
  private List<String> tags;

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
   * @return the documentId
   */
  public String getDocumentId() {
    return this.documentId;
  }

  /**
   * @param documentId
   *          the documentId to set
   */
  public void setDocumentId(String documentId) {
    this.documentId = documentId;
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
  public List<String> getTags() {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    return this.tags;
  }

  /**
   * @param tags the tags to set
   */
  public void setTags(List<String> tags) {
    this.tags = tags;
  }
}
