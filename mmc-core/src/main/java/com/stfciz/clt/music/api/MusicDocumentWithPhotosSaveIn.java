package com.stfciz.clt.music.api;

import java.io.Serializable;

import com.stfciz.clt.music.domain.MusicDocument;

/**
 * 
 * @author ByTel
 * 
 */
public class MusicDocumentWithPhotosSaveIn extends MusicDocument implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1082765332517662511L;

  private String            photosDir;

  private Boolean           upload;

  /**
   * @return the photosDir
   */
  public String getPhotosDir() {
    return this.photosDir;
  }

  /**
   * @param photosDir the photosDir to set
   */
  public void setPhotosDir(String photosDir) {
    this.photosDir = photosDir;
  }

  /**
   * @return the upload
   */
  public Boolean getUpload() {
    return this.upload;
  }

  /**
   * @param upload the upload to set
   */
  public void setUpload(Boolean upload) {
    this.upload = upload;
  }
}