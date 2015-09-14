package com.stfciz.mmc.core.photo.dao;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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
import com.flickr4java.flickr.photos.Size;
import com.flickr4java.flickr.photosets.Photosets;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.stfciz.mmc.core.photo.domain.Tag;
import com.stfciz.mmc.core.photo.flickr.FlickrConnect;
import com.stfciz.mmc.core.photo.oauth.FlickrOAuthContext;

/**
 * 
 * @author stfciz
 *
 */
@Component
public class FlickrApiImpl implements FlickrApi {

  private static final Logger LOGGER            = LoggerFactory.getLogger(FlickrApiImpl.class);

  private Set<String>         GET_PHOTOS_EXTRAS = new HashSet<>(Arrays.asList("url_t", "url_s", "url_m", "url_o","tags"));

  private FlickrConnect       flickrConnect;
  
  @Autowired
  public FlickrApiImpl(FlickrConnect flickrConnect) {
    this.flickrConnect = flickrConnect;
  }

  @Override
  @FlickrOAuthContext(Permission.READ_TYPE)
  public int getPhotosetCount() throws FlickrException {
    return this.flickrConnect.getFlickr().getPhotosetsInterface().getPhotosetCount(this.flickrConnect.getUserId());
  }

  @Override
  @FlickrOAuthContext(Permission.READ_TYPE)
  public Photosets getPhotosets() throws FlickrException {
    return this.flickrConnect.getFlickr().getPhotosetsInterface().getList(this.flickrConnect.getUserId());
  }
  
  @Override
  @FlickrOAuthContext(Permission.DELETE_TYPE)
  public void deletePhoto(String photoId) throws FlickrException {
    this.flickrConnect.getFlickr().getPhotosInterface().delete(photoId);
    LOGGER.debug("\"{}\" is deleted", photoId);
  }  

  @Override
  @FlickrOAuthContext(Permission.READ_TYPE)
  public PhotoList<Photo> getPhotos(String photoSetId, Integer aPerPage, Integer aPage) throws FlickrException {
    int perPage = aPerPage == null ? 500 : aPerPage;
    int page = aPage == null ? 1 : aPage;
    
    PhotoList<Photo> result = new PhotoList<Photo>();
    boolean oneMorePage = false;
    
    do {
      PhotoList<Photo> photos = this.flickrConnect.getFlickr().getPhotosetsInterface()
          .getPhotos(photoSetId, GET_PHOTOS_EXTRAS, Flickr.PRIVACY_LEVEL_NO_FILTER, perPage, page);
      
      if (photos != null) {
        result.addAll(photos);
        LOGGER.debug("{} photo(s) for photoSetId: {}, perPage: {}, page: {}", new Object[]{photos.getTotal(), photoSetId, perPage, page});
        Iterator<Photo> iterator = photos.iterator();
        while (iterator.hasNext()) {
          Photo photo = iterator.next();
          LOGGER.debug("id:{}, title:{}, url:{}", new Object[]{photo.getId(), photo.getTitle(), photo.getOriginalUrl()});
        }
        
        if (photos.getTotal() > photos.size()) {
          oneMorePage = true;
          page++;
        } else {
          oneMorePage = false;
        }
        
      } else {
        LOGGER.info("No photo for photoSetId: {}, perPage: {}, page: {}", new Object[]{photoSetId, perPage, page});
        oneMorePage = false;
      }
    } while (oneMorePage);
    
    return result;
  }

  @Override
  @FlickrOAuthContext(Permission.WRITE_TYPE)
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
      metaData.setTags(Collections2.transform(uploadPhoto.getTags(), new Function<Tag,String>(){
        @Override
        public String apply(Tag tag) {
         return tag.toFlickrFormat();
        }
      }));
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
      LOGGER.debug("The \"{}\" is added to the flickr album \"{}\".", new Object[] { responseId, uploadPhoto.getPhotoSetId() });
    }
    
    LOGGER.info("The photo \"{}\" is uploaded with success", new Object[] { responseId });
    
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
  @FlickrOAuthContext(Permission.READ_TYPE)
  public Photo getPhoto(String photoId) throws FlickrException {
    Photo photo = this.flickrConnect.getFlickr().getPhotosInterface().getPhoto(photoId);
    if (photo != null) {
      Collection<Size> sizes = this.flickrConnect.getFlickr().getPhotosInterface().getSizes(photoId);
      photo.setSizes(sizes);
    }
    return photo;
  }
}