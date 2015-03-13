package com.stfciz.mmc.web.controller.music;

import org.apache.commons.lang3.StringUtils;
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
import com.stfciz.mmc.core.CoreConfiguration;
import com.stfciz.mmc.core.music.MusicDocumentRepository;
import com.stfciz.mmc.core.music.domain.MusicDocument;
import com.stfciz.mmc.core.photo.dao.FlickrApi;
import com.stfciz.mmc.core.photo.dao.UploadPhoto;
import com.stfciz.mmc.core.photo.domain.Tag;
import com.stfciz.mmc.web.api.music.FindResponse;
import com.stfciz.mmc.web.api.music.GetResponse;
import com.stfciz.mmc.web.api.music.MusicApiConverter;
import com.stfciz.mmc.web.api.music.NewRequest;
import com.stfciz.mmc.web.api.music.UpdateRequest;
import com.stfciz.mmc.web.api.photo.PhotoApiConverter;
import com.stfciz.mmc.web.oauth2.OAuth2ScopeApi;
import com.stfciz.mmc.web.oauth2.Permission;

/**
 * 
 * @author stfciz
 *
 */
@RestController
@RequestMapping(value = "/music/md", produces = { MediaType.APPLICATION_JSON_VALUE })
public class MusicController {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(MusicController.class);

  @Autowired
  private MusicDocumentRepository repository;

  @Autowired
  private FlickrApi flickrApi;

  @Autowired
  private MusicApiConverter apiConverter;
  
  @Autowired
  private PhotoApiConverter photoApiConverter;

  @Autowired
  private CoreConfiguration configuration;

  @RequestMapping(method = RequestMethod.GET)
  @Permission(scopes = { OAuth2ScopeApi.READ })
  public FindResponse find(
      @RequestParam(value = "q", required = false) String input,
      @RequestParam(value = "p", required = false, defaultValue = "0") int page,
      @RequestParam(value = "s", required = false, defaultValue = "50") int pageSize) {

    PageRequest pageable = null;
    Page<MusicDocument> result = null;
    
    if (StringUtils.isBlank(input)) {
      pageable = new PageRequest(page, pageSize, new Sort(new  Sort.Order(Sort.Direction.DESC, "modified")));
      result = this.repository.findAll(pageable);
    } else {
      pageable = new PageRequest(page, pageSize, new Sort(new  Sort.Order(Sort.Direction.ASC, "title")));
      result = this.repository.search(input, pageable);
    }
    
    FindResponse response = new FindResponse();
    response.setPageSize(result.getSize());
    response.setNext(result.hasNext());
    response.setPrevious(result.hasPrevious());
    response.setPage(result.getNumber());
    response.setTotalPages(result.getTotalPages());

    if (result.hasContent()) {
      for (MusicDocument doc : result.getContent()) {
        response.getDocs().add(
            this.apiConverter.convertMusicDocumentToFindDocument(doc));
      }
    }
    return response;
  }

  @RequestMapping(method = RequestMethod.POST)
  @Permission(scopes = { OAuth2ScopeApi.WRITE })
  public ResponseEntity<GetResponse> create(@RequestBody(required = true) NewRequest req) {
    return new ResponseEntity<GetResponse>(this.apiConverter.convertMusicDocumentToGetResponse(
        this.repository.save(this.apiConverter.convertNewMusicRequestIn(req))), HttpStatus.CREATED);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.POST)
  @Permission(scopes = { OAuth2ScopeApi.WRITE})
  public ResponseEntity<GetResponse> update(@PathVariable String id, @RequestBody(required = true) UpdateRequest req) {
    return new ResponseEntity<GetResponse>(this.apiConverter.convertMusicDocumentToGetResponse(
          this.repository.save(this.apiConverter.convertUpdateMusicRequestIn(req))), HttpStatus.OK);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @Permission(scopes = { OAuth2ScopeApi.READ })
  public ResponseEntity<GetResponse> find(@PathVariable String id) {
    MusicDocument result = this.repository.findOne(id);
    if (result == null) {
      return new ResponseEntity<GetResponse>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<GetResponse>(
        this.apiConverter.convertMusicDocumentToGetResponse(result),
        HttpStatus.OK);
  }

  @RequestMapping(value = "/{id}/photos", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Permission(scopes = { OAuth2ScopeApi.WRITE })
  public ResponseEntity<String> uploadImage(@PathVariable String id, @RequestParam("file") MultipartFile file) throws Exception {
    MusicDocument doc = this.repository.findOne(id);
    if (doc == null) {
      LOGGER.error("Document \"{}\" not found");
      return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }
    
    if (file == null || file.isEmpty()) {
      LOGGER.error("No file to upload ...");
      return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }

    LOGGER.debug("Upload {} ...", file.getOriginalFilename());
    try {
      UploadPhoto uploadPhoto = new UploadPhoto();
      uploadPhoto.setAsync(false);
      uploadPhoto.setFilename(file.getOriginalFilename());
      uploadPhoto.getTags().add(new Tag("musicDoc", id));
      uploadPhoto.setPhotoSetId(this.configuration.getFlickr().getAppGalleryId());
      uploadPhoto.setContent(file.getBytes());
      
      String photoId = this.flickrApi.uploadPhoto(uploadPhoto);
      Photo photo = this.flickrApi.getPhoto(photoId);
      
      LOGGER.debug("photoId: {}", photoId);
      doc.getPhotos().add(this.photoApiConverter.convertToPhotoMusicDocument(photo, doc.getPhotos().size()));
      this.repository.save(doc);
      
    } catch (FlickrException flickrException) {
      LOGGER.error("save error", flickrException);
      return new ResponseEntity<String>(String.format(
          "Error when uploading %s", file.getOriginalFilename()),
          HttpStatus.SERVICE_UNAVAILABLE);
    }
    return new ResponseEntity<String>(HttpStatus.OK);
  }
}