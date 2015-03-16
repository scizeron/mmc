package com.stfciz.mmc.core.photo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.stfciz.mmc.core.photo.dao.FlickrApi;
import com.stfciz.mmc.core.photo.dao.UploadPhoto;

/**
 * 
 * @author stfciz
 *
 */
@Component
public class PhotoManagerImpl implements PhotoManager {
  
  @Autowired
  private FlickrApi               flickrApi;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(PhotoManager.class);
  
  @Override
  public void upload(String galleryId, byte [] bytes, String filename, boolean async) {
    try {
      UploadPhoto uploadPhoto = new UploadPhoto();
      uploadPhoto.setAsync(false);
      uploadPhoto.setFilename(filename);
      uploadPhoto.setPhotoSetId(galleryId);
      uploadPhoto.setContent(bytes);
      String photoId = this.flickrApi.uploadPhoto(uploadPhoto);
      LOGGER.debug("photoId: {}", photoId);
      
      Photo photo = this.flickrApi.getPhoto(photoId);
      LOGGER.debug("photoId: {}, url: {}", photoId, photo.getMedium800Url());
      
    } catch (FlickrException flickrException) {
      LOGGER.error("save error", flickrException);
      throw new RuntimeException("Error when uploading image", flickrException);
    }
  }

  @Override
  public void deletePhoto(String photoId) throws PhotoException{
    try {
      this.flickrApi.deletePhoto(photoId);
    } catch (FlickrException e) {
      throw new PhotoException("Error when deleting " + photoId, e);
    }
  }
}
