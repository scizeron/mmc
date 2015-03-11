package com.stfciz.mmc.core.photo;

/**
 * 
 * @author stfciz
 *
 */
public interface PhotoManager {

  /**
   * 
   * @param galleryId
   * @param bytes
   * @param filename
   * @param async
   */
  void upload(String galleryId, byte [] bytes, String filename, boolean async);
  
  /**
   * 
   * @param photoId
   * @throws PhotoException
   */
  void deletePhoto(String photoId) throws PhotoException;
}
