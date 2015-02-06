package com.stfciz.clt.photo.dao;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photosets.Photosets;

/**
 * 
 * @author ByTel
 *
 */
public interface FlickrApi {

  /**
   * 
   * @return
   * @throws FlickrException
   */
  int getPhotosetCount() throws FlickrException;
  
  /**
   * 
   * @return
   * @throws FlickrException
   */
  Photosets getPhotosets() throws FlickrException;
  
  /**
   * 
   * @param photoSetId
   * @param perPage
   * @param page
   * @return
   * @throws FlickrException
   */
  PhotoList<Photo> getPhotos(String photoSetId, Integer perPage, Integer page) throws FlickrException;
  
  /**
   * 
   * @param uploadPhoto
   * @return
   * @throws FlickrException
   */
  String uploadPhoto(UploadPhoto uploadPhoto) throws FlickrException;
  
  /**
   * 
   * @param photoId
   * @return
   * @throws FlickrException
   */
  Photo getPhoto(String photoId) throws FlickrException;
  
  /**
   * 
   * @param photoId
   * @throws FlickrException
   */
  void deletePhoto(String photoId) throws FlickrException;
}
