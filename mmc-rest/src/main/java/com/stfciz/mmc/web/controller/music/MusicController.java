package com.stfciz.mmc.web.controller.music;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
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
import com.stfciz.mmc.core.music.domain.PhotoMusicDocument;
import com.stfciz.mmc.core.photo.dao.FlickrApi;
import com.stfciz.mmc.core.photo.dao.UploadPhoto;
import com.stfciz.mmc.core.photo.domain.Tag;
import com.stfciz.mmc.web.api.music.FindResponse;
import com.stfciz.mmc.web.api.music.GetResponse;
import com.stfciz.mmc.web.api.music.MusicApiConverter;
import com.stfciz.mmc.web.api.music.NewRequest;
import com.stfciz.mmc.web.api.music.RemovePhotosIn;
import com.stfciz.mmc.web.api.music.UpdateRequest;
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

  @RequestMapping(method = RequestMethod.GET
      , consumes = { MediaType.ALL_VALUE }
      , produces = { MediaType.APPLICATION_JSON_VALUE, "application/pdf" })
  @Permission(scopes = { OAuth2ScopeApi.READ })
  public FindResponse find(
      @RequestParam(value = "q", required = false) String query,
      @RequestParam(value = "p", required = false, defaultValue = "0") int page,
      @RequestParam(value = "s", required = false, defaultValue = "50") int pageSize) {

    PageRequest pageable = null;
    Page<MusicDocument> result = null;
    boolean singlePage = (page == -1);
    boolean hasNext = false;
    
    FindResponse response = new FindResponse();
    response.setPageSize(0);
    
    do {
      
      if (singlePage) {
        page++;
        LOGGER.debug("\"single-page\" mode, page : {}", page);
      }
      
      if (StringUtils.isBlank(query)) {
        pageable = new PageRequest(page, pageSize, new Sort(new  Sort.Order(Sort.Direction.DESC, "modified")));
        result = this.repository.findAll(pageable);
      } else {
        pageable = new PageRequest(page, pageSize, new Sort(new  Sort.Order(Sort.Direction.ASC, "title")));
        QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder(query);
        queryBuilder.field("title");
        queryBuilder.field("artist");
        queryBuilder.defaultOperator(QueryStringQueryBuilder.Operator.OR);
        result = this.repository.search(queryBuilder, pageable);
      }
      
      hasNext = result.hasNext();
      response.setPageSize(response.getPageSize() + result.getSize());
      
      if (!singlePage) {
        response.setPrevious(result.hasPrevious());
        response.setPage(result.getNumber());
        response.setTotalPages(result.getTotalPages());
        response.setNext(hasNext);
      }
      
      if (result.hasContent()) {
        for (MusicDocument doc : result.getContent()) {
          response.getDocs().add(this.apiConverter.convertMusicDocumentToFindDocument(doc));
        }
      }
      
    } while (singlePage && hasNext);

    if (singlePage) {
      LOGGER.debug("\"single-page\" mode, {} item(s)", response.getDocs().size());
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
    MusicDocument partialDoc = this.apiConverter.convertUpdateMusicRequestIn(req);
    // docToUpdate ne contient pas les images, elles seront supprimées si update
    MusicDocument docToUpdate = this.repository.findOne(id);
    if (docToUpdate == null) {
     return new ResponseEntity<GetResponse>(HttpStatus.BAD_REQUEST);  
    }
    partialDoc.setPhotos(docToUpdate.getPhotos());
    return new ResponseEntity<GetResponse>(this.apiConverter.convertMusicDocumentToGetResponse(this.repository.save(partialDoc)), HttpStatus.OK);
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
  
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @Permission(scopes = { OAuth2ScopeApi.DELETE}, roles= { UserRole.ADMIN})
  public ResponseEntity<String> remove(@PathVariable String id) {
    MusicDocument result = this.repository.findOne(id);
    if (result == null) {
      return new ResponseEntity<String>(HttpStatus.OK);
    }
    
    List<PhotoMusicDocument> photos = result.getPhotos();
    if (photos != null && !photos.isEmpty()) {
      for (PhotoMusicDocument photo : photos) {
        try {
          this.flickrApi.deletePhoto(photo.getId());
        } catch (FlickrException e) {
          LOGGER.error("Error when deleting the \'{}\' photo. It wwill become orphan", e);
        }
      }
    }
    this.repository.delete(id);    
    return new ResponseEntity<String>((HttpStatus.OK));
  }

  @RequestMapping(value = "/{id}/photos", method = RequestMethod.DELETE)
  @Permission(scopes = { OAuth2ScopeApi.DELETE })
  public ResponseEntity<GetResponse> removePhotos(@PathVariable String id, @RequestBody(required=true) RemovePhotosIn in) throws Exception {
    MusicDocument doc = this.repository.findOne(id);
    if (doc == null) {
      LOGGER.error("\"{}\" not found");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }  
    
    List<PhotoMusicDocument> photos = doc.getPhotos();
    if (photos == null || photos.isEmpty()) {
      LOGGER.debug("No photo to remove");
      return new ResponseEntity<>(this.apiConverter.convertMusicDocumentToGetResponse(doc), HttpStatus.OK);
    }
    
    int initialSize = photos.size();
    
    for (int i = initialSize - 1; i >=0; i--) {
      for (String photoId : in.getIds()) {
        if (photos.get(i).getId().equals(photoId)) {
          photos.remove(i);
          break;
        }
      }
    }
    
    doc = this.repository.save(doc);
    
    for (String photoId : in.getIds()) {
      try {
        this.flickrApi.deletePhoto(photoId);
        LOGGER.error("The photo \"{}\" has been deleted infrom flickr", photoId);
      } catch (FlickrException flickrException) {
        LOGGER.error("delete error, the \"{}\" could not be removed", photoId, flickrException);
      }
    }
    
    return new ResponseEntity<>(this.apiConverter.convertMusicDocumentToGetResponse(doc), HttpStatus.OK);
  }
  
  @RequestMapping(value = "/{id}/photos", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Permission(scopes = { OAuth2ScopeApi.WRITE })
  public ResponseEntity<com.stfciz.mmc.web.api.photo.Photo> uploadImage(@PathVariable String id, @RequestParam("file") MultipartFile file) throws Exception {
    MusicDocument doc = this.repository.findOne(id);
    if (doc == null) {
      LOGGER.error("\"{}\" not found");
      return new ResponseEntity<com.stfciz.mmc.web.api.photo.Photo>(HttpStatus.BAD_REQUEST);
    }
    
    if (file == null || file.isEmpty()) {
      LOGGER.error("No file to upload ...");
      return new ResponseEntity<com.stfciz.mmc.web.api.photo.Photo>(HttpStatus.BAD_REQUEST);
    }

    Photo photo = null;
    try {
      UploadPhoto uploadPhoto = new UploadPhoto();
      uploadPhoto.setAsync(false);
      uploadPhoto.setFilename(file.getOriginalFilename());
      uploadPhoto.getTags().add(new Tag("musicDoc", id));
      uploadPhoto.setPhotoSetId(this.configuration.getFlickr().getAppGalleryId());
      uploadPhoto.setContent(file.getBytes());
      
      String photoId = this.flickrApi.uploadPhoto(uploadPhoto);
      photo = this.flickrApi.getPhoto(photoId);
    
      int order = doc.getPhotos().size();
      PhotoMusicDocument photoMusicDocument = this.photoApiConverter.convertToPhotoMusicDocument(photo, order);
      doc.getPhotos().add(photoMusicDocument);
      doc = this.repository.save(doc);
      
      LOGGER.debug("The photo \"{}\" is added to \"{}\"", photoId, doc.getId());
      
      com.stfciz.mmc.web.api.photo.Photo result =  this.photoApiConverter.convertPhotoMusicDocumentToPhotoApi(photoMusicDocument);
      return new ResponseEntity<com.stfciz.mmc.web.api.photo.Photo>(result, HttpStatus.OK);
      
    } catch (FlickrException flickrException) {
      LOGGER.error("Upload error", flickrException);
      return new ResponseEntity<com.stfciz.mmc.web.api.photo.Photo>(HttpStatus.SERVICE_UNAVAILABLE);
    }
  }
}