package com.stfciz.clt.web.controller;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stfciz.clt.CoreConfiguration;
import com.stfciz.clt.music.MusicDocumentManager;
import com.stfciz.clt.music.MusicManagerException;
import com.stfciz.clt.music.api.MusicDocumentGetIn;
import com.stfciz.clt.music.api.MusicDocumentOut;
import com.stfciz.clt.music.api.MusicDocumentWithPhotosSaveIn;
import com.stfciz.clt.music.api.MusicDocumentsOut;
import com.stfciz.clt.music.domain.MusicDocument;
import com.stfciz.clt.web.oauth2.OAuth2ScopeApi;
import com.stfciz.clt.web.oauth2.Permission;

@RestController
@RequestMapping(value="/music/md", produces = { MediaType.APPLICATION_JSON_VALUE })
public class MusicController {

  @Autowired
  private MusicDocumentManager musicDocumentManager;
  
  @Autowired
  private CoreConfiguration configuration;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(MusicController.class);
  
  @RequestMapping(method = RequestMethod.GET)
  @Permission(scopes = { OAuth2ScopeApi.READ })
  public MusicDocumentsOut find(
        @RequestParam(value = "q", required = false) String search
      , @RequestParam(value = "p", required = false, defaultValue = "0") int page
      , @RequestParam(value = "s", required = false, defaultValue = "50") int pageSize) throws MusicManagerException {
    MusicDocumentGetIn in = new MusicDocumentGetIn();
    in.setValue(search);
    in.setPage(page);
    in.setPageSize(pageSize);
    return this.musicDocumentManager.getMusicDocuments(in);
  }
 
  @RequestMapping(method = RequestMethod.POST)
  @Permission(scopes = { OAuth2ScopeApi.WRITE })
  @ResponseStatus(value=HttpStatus.CREATED)
  public MusicDocumentOut add(@RequestBody(required=true) MusicDocument doc ) throws MusicManagerException {
    LOGGER.debug("add : " + ToStringBuilder.reflectionToString(doc));
    MusicDocumentWithPhotosSaveIn in = new MusicDocumentWithPhotosSaveIn();
    BeanUtils.copyProperties(doc,  in);
    return this.musicDocumentManager.saveMusicDocumentWithPhotos(in);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.POST)
  @Permission(scopes = { OAuth2ScopeApi.WRITE })
  public MusicDocumentOut update(@PathVariable String id, @RequestBody(required=true) MusicDocument doc ) throws MusicManagerException {
    LOGGER.debug("update : " + ToStringBuilder.reflectionToString(doc));
    MusicDocumentWithPhotosSaveIn in = new MusicDocumentWithPhotosSaveIn();
    BeanUtils.copyProperties(doc,  in);
    return this.musicDocumentManager.saveMusicDocumentWithPhotos(in);
  }  
  
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @Permission(scopes = { OAuth2ScopeApi.READ })
  public MusicDocumentOut findById(@PathVariable String id) throws MusicManagerException {
    return this.musicDocumentManager.findById(id);
  }
  
  @RequestMapping(value = "/{id}/images", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Permission(scopes = { OAuth2ScopeApi.WRITE })
  @ResponseStatus(value=HttpStatus.CREATED)
  public void uploadImage(@PathVariable String id, @RequestParam("file") MultipartFile[] files) throws Exception {
    for (MultipartFile file : files) {
      LOGGER.debug("Upload {} ...", file.getOriginalFilename());
      this.musicDocumentManager.addPhoto(this.configuration.getFlickr().getAppGalleryId(), id, file.getBytes(), file.getOriginalFilename(), false);
    }
  }    
}