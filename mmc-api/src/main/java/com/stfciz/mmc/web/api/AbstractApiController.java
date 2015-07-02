package com.stfciz.mmc.web.api;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.stfciz.mmc.core.CoreConfiguration;
import com.stfciz.mmc.core.domain.DocumentType;
import com.stfciz.mmc.core.domain.MMCDocument;
import com.stfciz.mmc.core.photo.dao.FlickrApi;
import com.stfciz.mmc.core.photo.dao.UploadPhoto;
import com.stfciz.mmc.core.photo.domain.PhotoDocument;
import com.stfciz.mmc.core.photo.domain.Tag;
import com.stfciz.mmc.core.photo.domain.TagName;
import com.stfciz.mmc.web.api.photo.PhotoApiConverter;
import com.stfciz.mmc.web.oauth2.OAuth2ScopeApi;
import com.stfciz.mmc.web.oauth2.Permission;
import com.stfciz.mmc.web.oauth2.UserRole;
import com.stfciz.mmc.web.service.MMCService;
/**
 * 
 * @author Bellevue
 *
 */
public abstract class AbstractApiController<GR extends FindItemResponse, SR extends AbstractSaveRequest> {

  protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Autowired
  private ApplicationContext applicationContext;
  
  @Autowired
  private CoreConfiguration configuration;
  
  @Autowired
  private PhotoApiConverter photoApiConverter;
  
  @Autowired
  private FlickrApi flickrApi;
  
  @Autowired
  private ElasticsearchRepository<MMCDocument, String> repository;

  private ApiConverter<GR, SR> apiConverter;
  
  @Autowired
  private MMCService mmcService;
  
  private DocumentType supportedType;
  
  @SuppressWarnings("unchecked")
  @PostConstruct
  public void wireCollaborators() {
    final String domain = StringUtils.remove(this.getClass().getSimpleName(), "Controller").toLowerCase();
    this.supportedType = DocumentType.valueOf(domain.toUpperCase());
    String converterName = domain + "ApiConverter";
    LOGGER.debug("Wire '{}' for {} [{}]", new Object[]{converterName, this.getClass().getSimpleName(), this.supportedType});
    this.apiConverter = (ApiConverter<GR, SR>) this.applicationContext.getBean(converterName);
  }
  
  @RequestMapping(method = RequestMethod.POST)
  @Permission(scopes = { OAuth2ScopeApi.WRITE })
  public ResponseEntity<GR> create(@RequestBody(required = true) SR req) {
    return new ResponseEntity<GR>(this.apiConverter.convertToGetResponse(
        this.repository.save(this.apiConverter.convertFromNewRequest(req))), HttpStatus.CREATED);
  }
  
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @Permission(scopes = { OAuth2ScopeApi.DELETE}, roles= { UserRole.ADMIN})
  public ResponseEntity<String> remove(@PathVariable String id) {
    MMCDocument result = this.mmcService.findById(id, this.supportedType);
    if (result == null) {
      return new ResponseEntity<String>(HttpStatus.OK);
    }
    
    List<PhotoDocument> photos = result.getPhotos();
    if (photos != null && !photos.isEmpty()) {
      for (PhotoDocument photo : photos) {
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
  
  @RequestMapping(value = "/{id}", method = RequestMethod.POST)
  @Permission(scopes = { OAuth2ScopeApi.WRITE})
  public ResponseEntity<GR> update(@PathVariable String id, @RequestBody(required = true) SR req) {
    // docToUpdate ne contient pas les images, elles seront supprimées si update
    MMCDocument docToUpdate = this.mmcService.findById(id, this.supportedType);
    
    if (docToUpdate == null) {
     return new ResponseEntity<GR>(HttpStatus.BAD_REQUEST);  
    }
    
    MMCDocument partialDoc = this.apiConverter.convertFromUpdateRequest(req);
    partialDoc.setPhotos(docToUpdate.getPhotos());
    return new ResponseEntity<GR>(this.apiConverter.convertToGetResponse(this.repository.save(partialDoc)), HttpStatus.OK);
  }
  
  @RequestMapping(value = "/{id}/photos", method = RequestMethod.DELETE)
  @Permission(scopes = { OAuth2ScopeApi.DELETE })
  public ResponseEntity<GR> removePhotos(@PathVariable String id, @RequestBody(required=true) RemovePhotosRequest in) throws Exception {
    MMCDocument doc = this.mmcService.findById(id, this.supportedType);
    if (doc == null) {
      LOGGER.error("\"{}\" not found");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }  
    
    List<PhotoDocument> photos = doc.getPhotos();
    if (photos == null || photos.isEmpty()) {
      LOGGER.debug("No photo to remove");
      return new ResponseEntity<>(this.apiConverter.convertToGetResponse(doc), HttpStatus.OK);
    }
    
    int initialSize = photos.size();
    
    for (int i = initialSize - 1; i >=0; i--) {
      for (String photoId : in.getIds()) {
        if (photos.get(i).getId().equals(photoId)) {
          photos.remove(i);
        }
      }
    }
    
    doc = this.repository.save(doc);
    
    for (String photoId : in.getIds()) {
      try {
        this.flickrApi.deletePhoto(photoId);
        LOGGER.error("The photo \"{}\" has been deleted in flickr", photoId);
      } catch (FlickrException flickrException) {
        LOGGER.error("delete error, the \"{}\" could not be removed", photoId, flickrException);
      }
    }
    
    return new ResponseEntity<>(this.apiConverter.convertToGetResponse(doc), HttpStatus.OK);
  }
  
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @Permission(scopes = { OAuth2ScopeApi.READ })
  public ResponseEntity<GR> get(@PathVariable String id) {
    MMCDocument result = this.mmcService.findById(id, this.supportedType);
    if (result == null) {
      return new ResponseEntity<GR>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<GR>(
        this.apiConverter.convertToGetResponse(result),
        HttpStatus.OK);
  }
  
  @RequestMapping(method = RequestMethod.GET
      , consumes = { MediaType.ALL_VALUE }
      , produces = { MediaType.APPLICATION_JSON_VALUE, "application/pdf" })
  @Permission(scopes = { OAuth2ScopeApi.READ })
  public FindResponse search(
      @RequestParam(value = "q", required = false) String query,
      @RequestParam(value = "p", required = false, defaultValue = "0") int page,
      @RequestParam(value = "s", required = false, defaultValue = "50") int pageSize) {
    
    boolean allPages = (page == -1);
    
    if (!allPages) {
      return this.mmcService.search(query, page, pageSize, allPages, this.supportedType);
    }
    
   
    boolean hasNext = false;
    FindResponse findResponsePage = null;
    FindResponse response = new FindResponse();    
    
    do {
      page++;
      LOGGER.debug("page : {}", page);
      findResponsePage = this.mmcService.search(query, page, pageSize, allPages, this.supportedType);
      response.getItems().addAll(findResponsePage.getItems());
      hasNext = findResponsePage.isNext();
      
    } while (hasNext);

    LOGGER.debug("{} item(s)", response.getItems().size());
    return response;
  }
  
  @RequestMapping(value = "/{id}/photos", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Permission(scopes = { OAuth2ScopeApi.WRITE })
  public ResponseEntity<com.stfciz.mmc.web.api.photo.Photo> uploadPhoto(@PathVariable String id, @RequestParam("file") MultipartFile file) throws Exception {
    MMCDocument doc = this.mmcService.findById(id, this.supportedType);
    
    //String galleryId = this.configuration.getFlickr().get
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
      uploadPhoto.getTags().add(new Tag(TagName.DOC_ID, id));
      uploadPhoto.setPhotoSetId(this.configuration.getFlickr().getGalleryId(this.supportedType.name().toLowerCase()));
      uploadPhoto.setContent(file.getBytes());
      
      String photoId = this.flickrApi.uploadPhoto(uploadPhoto);
      photo = this.flickrApi.getPhoto(photoId);
    
      int order = doc.getPhotos().size();
      PhotoDocument photoDocument = this.photoApiConverter.convertToPhotoMusicDocument(photo, order);
      doc.getPhotos().add(photoDocument);
      doc = this.repository.save(doc);
      
      LOGGER.debug("The photo \"{}\" is added to \"{}\"", photoId, doc.getId());
      
      com.stfciz.mmc.web.api.photo.Photo result =  this.photoApiConverter.convertPhotoDocument(photoDocument);
      return new ResponseEntity<com.stfciz.mmc.web.api.photo.Photo>(result, HttpStatus.OK);
      
    } catch (FlickrException flickrException) {
      LOGGER.error("Upload error", flickrException);
      return new ResponseEntity<com.stfciz.mmc.web.api.photo.Photo>(HttpStatus.SERVICE_UNAVAILABLE);
    }
  }
}
