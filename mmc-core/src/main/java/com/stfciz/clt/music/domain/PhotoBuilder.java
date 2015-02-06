package com.stfciz.clt.music.domain;

import com.flickr4java.flickr.FlickrException;

/**
 * 
 * @author stcizero
 *
 */
public class PhotoBuilder {

  private Photo photo;
  
  public PhotoBuilder fromFlickr(com.flickr4java.flickr.photos.Photo src) {
   this.photo = new Photo(src.getId());
   
   try {
    this.photo.setThumbUrl(src.getThumbnailUrl());
    this.photo.setOriginalUrl(src.getOriginalUrl());
    this.photo.setMediumUrl(src.getMediumUrl());
  } catch (FlickrException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
   
   return this; 
  }
  
  
  public Photo build() {
    return this.photo;
  }
}
