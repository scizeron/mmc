package com.stfciz.clt.photo.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
import com.stfciz.clt.photo.FlickrConnect;
import com.stfciz.clt.photo.oauth.OAuthContext;

@Component
public class FlickrApiImpl implements FlickrApi {

  private static final Logger LOGGER            = LoggerFactory.getLogger(FlickrApiImpl.class);

  private Set<String>         GET_PHOTOS_EXTRAS = new HashSet<>(Arrays.asList("url_t", "url_s", "url_m", "url_o"));

  private FlickrConnect       flickrConnect;

  @Autowired
  public FlickrApiImpl(FlickrConnect flickrConnect) {
    this.flickrConnect = flickrConnect;
  }

  @Override
  @OAuthContext(Permission.READ_TYPE)
  public int getPhotosetCount() throws FlickrException {
    return this.flickrConnect.getFlickr().getPhotosetsInterface().getPhotosetCount(this.flickrConnect.getUserId());
  }

  @Override
  @OAuthContext(Permission.READ_TYPE)
  public Photosets getPhotosets() throws FlickrException {
    return this.flickrConnect.getFlickr().getPhotosetsInterface().getList(this.flickrConnect.getUserId());
  }
  
  @Override
  @OAuthContext(Permission.DELETE_TYPE)
  public void deletePhoto(String photoId) throws FlickrException {
    this.flickrConnect.getFlickr().getPhotosInterface().delete(photoId);
  }  

  @Override
  @OAuthContext(Permission.READ_TYPE)
  public PhotoList<Photo> getPhotos(String photoSetId, Integer aPerPage, Integer aPage) throws FlickrException {
    int perPage = aPerPage == null ? -1 : aPerPage;
    int page = aPage == null ? -1 : aPage;
    
    PhotoList<Photo> photos = this.flickrConnect.getFlickr().getPhotosetsInterface()
        .getPhotos(photoSetId, GET_PHOTOS_EXTRAS, Flickr.PRIVACY_LEVEL_NO_FILTER, perPage, page);
    
    if (photos != null) {
      LOGGER.debug("{} photo(s) for photoSetId: {}, perPage: {}, page: {}", new Object[]{photos.getTotal(), photoSetId, perPage, page});
      Iterator<Photo> iterator = photos.iterator();
      while (iterator.hasNext()) {
        Photo photo = iterator.next();
        LOGGER.debug("id:{}, title:{}, url_c:{}", new Object[]{photo.getId(), photo.getTitle(), photo.getMedium800Url()});
      }
    } else {
      LOGGER.info("No photo for photoSetId: {}, perPage: {}, page: {}", new Object[]{photoSetId, perPage, page});
    }
    
    return photos;
  }

  @Override
  @OAuthContext(Permission.WRITE_TYPE)
  public String uploadPhoto(UploadPhoto uploadPhoto) throws FlickrException {
    final String filename = new File(uploadPhoto.getFilename()).getName().toLowerCase();
    UploadMetaData metaData = new UploadMetaData();
    metaData.setPublicFlag(false);
    metaData.setFamilyFlag(false);
    metaData.setFriendFlag(false);
    metaData.setFilename(filename);
    metaData.setTitle(uploadPhoto.getTitle() != null ? uploadPhoto.getTitle() : filename);
    metaData.setAsync(uploadPhoto.isAsync());
    if (CollectionUtils.isNotEmpty(uploadPhoto.getTags())) {
      metaData.setTags(uploadPhoto.getTags());
    }
    
    // identifiant du document
    if (uploadPhoto.getDocumentId() != null) {
      List<String> tags = new ArrayList<String>();
      tags.add(String.format("music:id=%s", uploadPhoto.getDocumentId()));
      metaData.setTags(tags);
    }

    Uploader uploader = this.flickrConnect.getFlickr().getUploader();
    
    setFilemimeType(filename, metaData);

    String responseId;
    LOGGER.debug("Uploading \"{}\" ...", filename);

    if (uploadPhoto.getContent() != null) {
      responseId = uploader.upload(uploadPhoto.getContent(), metaData);
    } else {
      responseId = uploader.upload(new File(uploadPhoto.getFilename()), metaData);
    }

    if (!uploadPhoto.isAsync()) {
      this.flickrConnect.getFlickr().getPhotosetsInterface().addPhoto(uploadPhoto.getPhotoSetId(), responseId);
      LOGGER.debug("The \"{}\" img is added to the flickr album \"{}\".", new Object[] { responseId, uploadPhoto.getPhotoSetId() });
    }
    
    LOGGER.info("Upload OK - img:\"{}\"", new Object[] { responseId });
    
    return responseId;
  }

  /**
   * 
   * @param filename
   * @param metaData
   */
  private void setFilemimeType(final String filename, UploadMetaData metaData) {
    String filenameToLowerCase = filename.toLowerCase();
    if (filenameToLowerCase.endsWith(".png")) {
      metaData.setFilemimetype("image/png");
    } else if (filenameToLowerCase.endsWith(".jpeg") || filename.endsWith(".jpg")) {
      metaData.setFilemimetype("image/jpeg");
    } else if (filenameToLowerCase.endsWith(".gif")) {
      metaData.setFilemimetype("image/gif");
    }
  }

  @Override
  @OAuthContext(Permission.READ_TYPE)
  public Photo getPhoto(String photoId) throws FlickrException {
    return this.flickrConnect.getFlickr().getPhotosInterface().getPhoto(photoId);
  }
}