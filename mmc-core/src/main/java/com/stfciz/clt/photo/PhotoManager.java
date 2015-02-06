package com.stfciz.clt.photo;

import com.flickr4java.flickr.FlickrException;

/**
 * 
 * @author ByTel
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
   */
  void deletePhoto(String photoId) throws FlickrException;
}
