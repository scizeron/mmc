package com.stfciz.clt.web.controller;


import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.stfciz.clt.CoreConfiguration;
import com.stfciz.clt.music.MusicDocumentRepository;
import com.stfciz.clt.music.domain.Image;
import com.stfciz.clt.music.domain.MusicDocument;
import com.stfciz.clt.photo.dao.FlickrApi;
import com.stfciz.clt.photo.dao.UploadPhoto;
import com.stfciz.clt.web.api.music.FindResponse;
import com.stfciz.clt.web.api.music.GetResponse;
import com.stfciz.clt.web.api.music.NewRequest;
import com.stfciz.clt.web.api.music.SaveRequest;
import com.stfciz.clt.web.converter.ApiConverter;
import com.stfciz.clt.web.oauth2.OAuth2ScopeApi;
import com.stfciz.clt.web.oauth2.Permission;

/**
 * 
 * @author stfciz
 *
 */
@RestController
@RequestMapping(value="/music/md", produces = { MediaType.APPLICATION_JSON_VALUE })
public class MusicController {

  @Autowired
  private MusicDocumentRepository repository;

  @Autowired
  private FlickrApi               flickrApi;
  
  @Autowired 
  private ApiConverter apiConverter;
  
  @Autowired
  private CoreConfiguration configuration;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(MusicController.class);
  
  @RequestMapping(method = RequestMethod.GET)
  @Permission(scopes = { OAuth2ScopeApi.READ })
  public FindResponse find(
        @RequestParam(value = "q", required = false) String search
      , @RequestParam(value = "p", required = false, defaultValue = "0") int page
      , @RequestParam(value = "s", required = false, defaultValue = "50") int pageSize) {
   
    PageRequest pageable = new PageRequest(page, pageSize, new Sort(new Sort.Order(Sort.Direction.ASC,"title")));
    Page<MusicDocument> result = this.repository.findAll(pageable);
    
    FindResponse response = new FindResponse();
    response.setPageSize(result.getSize());
    response.setNext(result.hasNext());
    response.setPrevious(result.hasPrevious());
    response.setPage(result.getNumber());
    response.setTotalPages(result.getTotalPages());
    
    if (result.hasContent()) {
      for (MusicDocument doc : result.getContent()) {
        response.getDocs().add(this.apiConverter.convertMusicDocumentToFindDocument(doc));
      }
    }
    return response;
 }
 
  @RequestMapping(method = RequestMethod.POST)
  @Permission(scopes = { OAuth2ScopeApi.WRITE })
  public ResponseEntity<GetResponse> create(@RequestBody(required=true) NewRequest req )  {
    MusicDocument doc = this.apiConverter.convertNewMusicRequestIn(req);
    doc.setId(UUID.randomUUID().toString());
    return new ResponseEntity<GetResponse>(
        this.apiConverter.convertMusicDocumentToGetResponse(this.repository.save(doc)),HttpStatus.CREATED);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.POST)
  @Permission(scopes = { OAuth2ScopeApi.WRITE })
  public ResponseEntity<GetResponse> update(@PathVariable String id, @RequestBody(required=true) SaveRequest req )  {
    return new ResponseEntity<GetResponse>(
        this.apiConverter.convertMusicDocumentToGetResponse(
            this.repository.save(this.apiConverter.convertNewMusicRequestIn(req))),HttpStatus.OK);
    
  }  
 
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @Permission(scopes = { OAuth2ScopeApi.READ })
  public ResponseEntity<GetResponse> find(@PathVariable String id) {
    MusicDocument result = this.repository.findOne(id);
    if (result == null) {
      return new ResponseEntity<GetResponse>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<GetResponse>(this.apiConverter.convertMusicDocumentToGetResponse(result), HttpStatus.OK);
  }
  
  @RequestMapping(value = "/{id}/images", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Permission(scopes = { OAuth2ScopeApi.WRITE })
  public ResponseEntity<String> uploadImage(@PathVariable String id, @RequestParam("file") MultipartFile[] files) throws Exception {
    MusicDocument doc = this.repository.findOne(id);
    if (doc == null){
      return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }
    
    for (MultipartFile file : files) {
      LOGGER.debug("Upload {} ...", file.getOriginalFilename());
      try {
        UploadPhoto uploadPhoto = new UploadPhoto();
        uploadPhoto.setAsync(false);
        uploadPhoto.setFilename(file.getOriginalFilename());
        uploadPhoto.setDocumentId(id);
        uploadPhoto.setPhotoSetId(this.configuration.getFlickr().getAppGalleryId());
        uploadPhoto.setContent(file.getBytes());
        String photoId = this.flickrApi.uploadPhoto(uploadPhoto);
        Photo photo = this.flickrApi.getPhoto(photoId);
        LOGGER.debug("photoId: {}", photoId);
        doc.getImages().add(new Image(photo.getMediumUrl(), doc.getImages().size()));
        this.repository.save(doc);
      } catch (FlickrException flickrException) {
        LOGGER.error("save error", flickrException);
        return new ResponseEntity<String>(String.format("Error when uploading %s", file.getOriginalFilename()), HttpStatus.SERVICE_UNAVAILABLE);
      }      
    }
    return new ResponseEntity<String>(HttpStatus.OK);
  }   
}