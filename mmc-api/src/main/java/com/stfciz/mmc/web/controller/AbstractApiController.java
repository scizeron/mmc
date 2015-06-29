package com.stfciz.mmc.web.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.stfciz.mmc.core.domain.AbstractDocument;
import com.stfciz.mmc.core.photo.dao.FlickrApi;
import com.stfciz.mmc.core.photo.dao.UploadPhoto;
import com.stfciz.mmc.core.photo.domain.PhotoDocument;
import com.stfciz.mmc.core.photo.domain.Tag;
import com.stfciz.mmc.core.photo.domain.TagName;
import com.stfciz.mmc.web.api.AbstractApiConverter;
import com.stfciz.mmc.web.api.AbstractBaseResponse;
import com.stfciz.mmc.web.api.AbstractFindResponse;
import com.stfciz.mmc.web.api.AbstractNewRequest;
import com.stfciz.mmc.web.api.RemovePhotosRequestContent;
import com.stfciz.mmc.web.api.photo.PhotoApiConverter;
import com.stfciz.mmc.web.oauth2.OAuth2ScopeApi;
import com.stfciz.mmc.web.oauth2.Permission;
import com.stfciz.mmc.web.oauth2.UserRole;
import com.stfciz.mmc.web.service.FindRequestHandler;
/**
 * 
 * @author Bellevue
 *
 */
public abstract class AbstractApiController<D extends AbstractDocument, GR extends AbstractBaseResponse, NR extends AbstractNewRequest, UR extends AbstractNewRequest, FER extends AbstractBaseResponse, FR extends AbstractFindResponse<FER>> {

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
  private ElasticsearchRepository<D, String> repository;
  
  private AbstractApiConverter<D, GR, NR, UR, FER, FR> apiConverter;
  
  private FindRequestHandler findRequestQueryHander;
 
  @SuppressWarnings("unchecked")
  @PostConstruct
  public void wireCollaborators() {
    String domain = StringUtils.remove(this.getClass().getSimpleName(), "Controller").toLowerCase();

    String converterName = domain + "ApiConverter";
    LOGGER.debug("Wire '{}' for {}", converterName, this);
    this.apiConverter = (AbstractApiConverter<D, GR, NR, UR, FER, FR>) this.applicationContext.getBean(converterName);
    
    String findRequestHandlerName = domain + "FindRequestHandler";
    LOGGER.debug("Wire '{}' for {}", findRequestHandlerName, this);
    this.findRequestQueryHander = (FindRequestHandler) this.applicationContext.getBean(findRequestHandlerName);
  }
  
  @RequestMapping(method = RequestMethod.POST)
  @Permission(scopes = { OAuth2ScopeApi.WRITE })
  public ResponseEntity<GR> create(@RequestBody(required = true) NR req) {
    return new ResponseEntity<GR>(this.apiConverter.convertToGetResponse(
        this.repository.save(this.apiConverter.convertNewRequestContentToDcoument(req))), HttpStatus.CREATED);
  }
  
  
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @Permission(scopes = { OAuth2ScopeApi.DELETE}, roles= { UserRole.ADMIN})
  public ResponseEntity<String> remove(@PathVariable String id) {
    AbstractDocument result = this.repository.findOne(id);
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
  public ResponseEntity<GR> update(@PathVariable String id, @RequestBody(required = true) UR req) {
    D partialDoc = this.apiConverter.convertUpdateRequestContent(req);
    // docToUpdate ne contient pas les images, elles seront supprimées si update
    D docToUpdate = this.repository.findOne(id);
    if (docToUpdate == null) {
     return new ResponseEntity<GR>(HttpStatus.BAD_REQUEST);  
    }
    partialDoc.setPhotos(docToUpdate.getPhotos());
    return new ResponseEntity<GR>(this.apiConverter.convertToGetResponse(this.repository.save(partialDoc)), HttpStatus.OK);
  }
  
  @RequestMapping(value = "/{id}/photos", method = RequestMethod.DELETE)
  @Permission(scopes = { OAuth2ScopeApi.DELETE })
  public ResponseEntity<GR> removePhotos(@PathVariable String id, @RequestBody(required=true) RemovePhotosRequestContent in) throws Exception {
    D doc = this.repository.findOne(id);
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
  public ResponseEntity<GR> find(@PathVariable String id) {
    D result = this.repository.findOne(id);
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
  public FR find(
      @RequestParam(value = "q", required = false) String query,
      @RequestParam(value = "i", required = false) String index,
      @RequestParam(value = "p", required = false, defaultValue = "0") int page,
      @RequestParam(value = "s", required = false, defaultValue = "50") int pageSize) {
    
    if (index == null) {
      index = "music";
    }
    
    PageRequest pageable = null;
    Page<D> result = null;
    boolean singlePage = (page == -1);
    boolean hasNext = false;
    
    FR response = this.apiConverter.newFindResponse();

    response.setPageSize(0);
    
    do {
      
      if (singlePage) {
        page++;
        LOGGER.debug("\"single-page\" mode, page : {}", page);
      }
      
      if (StringUtils.isBlank(query)) {
        Sort sort = new Sort(new  Sort.Order(Sort.Direction.DESC, "modified"));
        if (singlePage) {
          sort = this.findRequestQueryHander.getSort(index);
        }
        
        result = this.repository.findAll(new PageRequest(page, pageSize, sort));
      } else {
        pageable = new PageRequest(page, pageSize, this.findRequestQueryHander.getSort(index));
        QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder(query);
        this.findRequestQueryHander.customizeQueryStringQueryBuilder(queryBuilder);
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
        for (D doc : result.getContent()) {
          response.getItems().add(this.apiConverter.convertToFindElementResponse(doc));
        }
      }
      
    } while (singlePage && hasNext);

    if (singlePage) {
      LOGGER.debug("\"single-page\" mode, {} item(s)", response.getItems().size());
    }
    
    return response;
  }
  
  @RequestMapping(value = "/{id}/photos", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Permission(scopes = { OAuth2ScopeApi.WRITE })
  public ResponseEntity<com.stfciz.mmc.web.api.photo.Photo> uploadPhoto(@PathVariable String id, @RequestParam("file") MultipartFile file) throws Exception {
    D doc = this.repository.findOne(id);
    
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
      uploadPhoto.setPhotoSetId(this.configuration.getFlickr().getGalleryId(StringUtils.remove(doc.getClass().getSimpleName(), "Document").toLowerCase()));
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
