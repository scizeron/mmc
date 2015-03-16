package com.stfciz.mmc.web.controller.photo;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.stfciz.mmc.core.CoreConfiguration;
import com.stfciz.mmc.core.photo.PhotoManager;
import com.stfciz.mmc.core.photo.dao.FlickrApi;
import com.stfciz.mmc.web.api.photo.PhotoApiConverter;
import com.stfciz.mmc.web.oauth2.OAuth2ScopeApi;
import com.stfciz.mmc.web.oauth2.Permission;
import com.stfciz.mmc.web.oauth2.UserRole;

/**
 * 
 * @author stfciz
 *
 */
@RestController
public class PhotoController {

  private static final Logger LOGGER = LoggerFactory.getLogger(PhotoController.class);
  
  @Autowired
  private CoreConfiguration coreConfiguration;

  @Autowired
  private PhotoApiConverter converter;
  
  @Autowired
  private PhotoManager photoManager;

  @Autowired
  private FlickrApi    flickrApi;

  @RequestMapping(value = "/photosets", method = RequestMethod.GET)
  @Permission(scopes={OAuth2ScopeApi.READ})
  public String get() throws FlickrException {
    return String.valueOf(this.flickrApi.getPhotosetCount());
  }
  
  @RequestMapping(value = "/photosets/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
  @Permission(scopes={OAuth2ScopeApi.READ})
  public List<com.stfciz.mmc.web.api.photo.Photo> getPhotos(@PathVariable(value = "id") String id,
      @RequestParam(value = "perPage", required = false) Integer perPage, @RequestParam(value = "page", required = false) Integer page) throws FlickrException {
    try {
      PhotoList<Photo> photos = null;
      if ("appgallery".equals(id)) {
        photos = this.flickrApi.getPhotos(this.coreConfiguration.getFlickr().getAppGalleryId(), perPage, page);
      } else {
        photos = this.flickrApi.getPhotos(id, perPage, page);
      }
      ListIterator<Photo> photosIt = photos.listIterator();
      List<com.stfciz.mmc.web.api.photo.Photo> result = new ArrayList<com.stfciz.mmc.web.api.photo.Photo>(photos.getTotal());
      while (photosIt.hasNext()) {
        result.add(this.converter.convertPhotoDomain(photosIt.next()));
      }
      return result;
    } catch(FlickrException e ) {
      if ("Photoset not found".equals(e.getErrorCode())) {
        return new ArrayList<>();
      } else {
        throw e;
      }
    }
  }
  
  @RequestMapping(value = "/photosets/{id}", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Permission(scopes = {OAuth2ScopeApi.WRITE}, roles = {UserRole.WRITE})
  @ResponseStatus(value=HttpStatus.CREATED)
  public void addPhoto(@PathVariable String id, @RequestParam("file") MultipartFile[] files) throws Exception {
    for (MultipartFile file : files) {
      LOGGER.debug("Upload {} ...", file.getOriginalFilename());
      this.photoManager.upload(id, file.getBytes(), file.getOriginalFilename(), false);
    }
  }   
  
  @RequestMapping(value = "/photo/{id}", method = RequestMethod.DELETE, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Permission(scopes = {OAuth2ScopeApi.WRITE}, roles = {UserRole.ADMIN})
  @ResponseStatus(value=HttpStatus.OK)
  public void deletePhoto(@PathVariable String photoId) throws Exception {
    this.photoManager.deletePhoto(photoId);
  }   
}
