package com.stfciz.clt.photo.flickr;

import com.flickr4java.flickr.Flickr;

/**
 * 
 * @author stfciz
 *
 */
public interface FlickrConnect {

  Flickr getFlickr() ;
  
  String getUserId();
  
}
