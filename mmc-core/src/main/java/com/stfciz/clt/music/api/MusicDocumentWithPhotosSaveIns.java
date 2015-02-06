package com.stfciz.clt.music.api;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author ByTel
 * 
 */
public class MusicDocumentWithPhotosSaveIns implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6906840532186383716L;

  private String                              photosDir;

  private Boolean                             upload;

  private List<MusicDocumentWithPhotosSaveIn> docs;

  /**
   * @return the in
   */
  public List<MusicDocumentWithPhotosSaveIn> getDocs() {
    return this.docs;
  }

  /**
   * @param in
   *          the in to set
   */
  public void setDocs(List<MusicDocumentWithPhotosSaveIn> docs) {
    this.docs = docs;
  }

  /**
   * @return the photosDir
   */
  public String getPhotosDir() {
    return this.photosDir;
  }

  /**
   * @param photosDir
   *          the photosDir to set
   */
  public void setPhotosDir(String photosDir) {
    this.photosDir = photosDir;
  }

  /**
   * @return the upload
   */
  public Boolean isUpload() {
    return this.upload != null ? this.upload : null;
  }

  /**
   * @param upload
   *          the upload to set
   */
  public void setUpload(Boolean upload) {
    this.upload = upload;
  }
}
