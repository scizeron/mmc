package com.stfciz.clt.music.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.stfciz.clt.CoreConfiguration;
import com.stfciz.clt.music.MusicDocumentManager;
import com.stfciz.clt.music.MusicManagerException;
import com.stfciz.clt.music.api.MusicDocumentGetIn;
import com.stfciz.clt.music.api.MusicDocumentOut;
import com.stfciz.clt.music.api.MusicDocumentWithPhotosSaveIn;
import com.stfciz.clt.music.api.MusicDocumentWithPhotosSaveIns;
import com.stfciz.clt.music.api.MusicDocumentsOut;
import com.stfciz.clt.music.domain.Image;
import com.stfciz.clt.music.domain.MusicDocument;
import com.stfciz.clt.photo.FlickrUtils;
import com.stfciz.clt.photo.dao.FlickrApi;
import com.stfciz.clt.photo.dao.UploadPhoto;

@Component
public class MusicDocumentManagerImpl implements MusicDocumentManager {

  private static final Logger     LOGGER = LoggerFactory.getLogger(MusicDocumentManagerImpl.class);

  @Autowired
  private MusicDocumentRepository repository;

  @Autowired
  private ElasticsearchTemplate   template;

  @Autowired
  private CoreConfiguration        configuration;

  @Autowired
  private FlickrApi               flickrApi;
  
  @Autowired
  public MusicDocumentManagerImpl(MusicDocumentRepository documentRepository) {
    this.repository = documentRepository;
  }

  @Override
  public MusicDocumentOut findById(String id) throws MusicManagerException {
    LOGGER.debug("Search \"{}\"", id);
    return convertToMusicDocumentOut(this.repository.findOne(id));
  }
  
  @Override
  public MusicDocumentsOut getMusicDocuments(MusicDocumentGetIn in) {
    LOGGER.debug("Search \"{}\" - page:{}, pageSize:{} ...", in.getValue(), in.getPage(), in.getPageSize());
    
    PageRequest pageable = new PageRequest(in.getPage(), in.getPageSize(), new Sort(new Sort.Order(Sort.Direction.ASC,"title")));
    Page<MusicDocument> page = this.repository.findAll(pageable);
    if (StringUtils.isBlank(in.getValue())) {
      page = this.repository.findAll(pageable);
    } else {
      page = this.repository.findByTitleIgnoreCase(in.getValue(),pageable);
    }
    
    MusicDocumentsOut result = new MusicDocumentsOut();
    result.setPageSize(in.getPageSize());
    result.setNext(page.hasNext());
    result.setPrevious(page.hasPrevious());
    result.setPage(page.getNumber());
    result.setTotalPages(page.getTotalPages());

    if (page.hasContent()) {
      for (MusicDocument doc : page.getContent()) {
        result.getDocs().add(convertToMusicDocumentOut(doc));
      }
    }
    
    LOGGER.debug("Search \"{}\": {} item(s), page:{}/{}, previous:{}, next:{}",
        new Object[] { in.getValue(), page.getNumberOfElements()
                                    , page.getNumber()
                                    , page.getTotalPages() - 1
                                    , page.hasPrevious()
                                    , page.hasNext()});
    
    return result;
  }

  /**
   * 
   * @param doc
   * @return
   */
  MusicDocumentOut convertToMusicDocumentOut(MusicDocument doc) {
    if (doc == null) {
      return null;
    }
    MusicDocumentOut out = new MusicDocumentOut();
    BeanUtils.copyProperties(doc, out);
    out.setThumbImageUrl(FlickrUtils.getThumbPhotoUrl(out));
    return out;
  }
  
  @Override
  public MusicDocumentOut saveMusicDocumentWithPhotos(MusicDocumentWithPhotosSaveIn md) throws MusicManagerException {
    MusicDocument doc = md;
    boolean create = false;
    
    if (md.getId() == null) {
      create = true;
      String id = UUID.randomUUID().toString();
      doc.setId(id);  
    }
    
    MusicDocument newDoc = this.repository.save(doc);
    
    if (create) {
      LOGGER.debug("\"{}\" is created : {}", newDoc.getId(), ToStringBuilder.reflectionToString(newDoc));
    } else {
      LOGGER.debug("\"{}\" is upated : {}", newDoc.getId(), ToStringBuilder.reflectionToString(newDoc));
    }
    
    return convertToMusicDocumentOut(newDoc);
  }
    
//    if (md.getImages() != null && !md.getImages().isEmpty()) {
//      for (int i = 0; i < md.getImages().size(); i++) {
//        String image = md.getImages().get(i);
//        doc.getImages().add(new Image(image, i));
//      }
//    }
    
//    if (update) {
//      // update
//      MusicDocument existing = this.repository.findOne(md.getId());
//      if (existing == null) {
//        doc.setId(UUID.randomUUID().toString());
//        LOGGER.warn("{} doesn't exist, create a new one \"{}\"", md.getId(), doc.getId());
//      } else {
//        // on met a jour les champs ...
//        LOGGER.warn("{} will be updated", md.getId());
//      }
//    }
//    
    // si upload
//    if ((md.getUpload() == null || md.getUpload().booleanValue()) && md.getImages() != null && !md.getImages().isEmpty()) {
//      for (int i = 0; i < md.getImages().size(); i++) {
//        try {
//          String filename = md.getImages().get(i);
//          if (filename.startsWith("http")) {
//            continue;
//          }
//          
//          UploadPhoto uploadPhoto = new UploadPhoto();
//          uploadPhoto.setAsync(false);
//          uploadPhoto.setDocumentId(documentId);
//          uploadPhoto.setFilename(filename);
//          uploadPhoto.setPhotoSetId(this.appConfiguration.getFlickr().getAppGalleryId());
//          uploadPhoto.setTitle(doc.getTitle());
//  
//          String photoId = this.flickrApi.uploadPhoto(uploadPhoto);
//          Photo photo = this.flickrApi.getPhoto(photoId);
//          doc.getImages().add(new Image(photo.getMedium800Url(), i));
//
//        } catch (FlickrException flickrException) {
//          LOGGER.error("save error", flickrException);
//        }
//      }
//    }
  
  @Override
  public void addPhoto(String galleryId, byte [] bytes, String filename, boolean async) throws MusicManagerException {
    
    try {
      UploadPhoto uploadPhoto = new UploadPhoto();
      uploadPhoto.setAsync(false);
      uploadPhoto.setFilename(filename);
      uploadPhoto.setPhotoSetId(galleryId);
      uploadPhoto.setContent(bytes);
      String photoId = this.flickrApi.uploadPhoto(uploadPhoto);
      LOGGER.debug("photoId: {}", photoId);
      
    } catch (FlickrException flickrException) {
      LOGGER.error("save error", flickrException);
      throw new MusicManagerException("Error when uploading image", flickrException);
    }
  }  
  
  @Override
  public void addPhoto(String galleryId, String docId, byte [] bytes, String filename, boolean async) throws MusicManagerException {
    MusicDocument doc = this.repository.findOne(docId);
    if (doc == null){
      return;
    }
    
    try {
      UploadPhoto uploadPhoto = new UploadPhoto();
      uploadPhoto.setAsync(false);
      uploadPhoto.setFilename(filename);
      uploadPhoto.setDocumentId(docId);
      uploadPhoto.setPhotoSetId(galleryId);
      uploadPhoto.setContent(bytes);
      String photoId = this.flickrApi.uploadPhoto(uploadPhoto);
      Photo photo = this.flickrApi.getPhoto(photoId);
      LOGGER.debug("photoId: {}", photoId);
      doc.getImages().add(new Image(photo.getMediumUrl(), doc.getImages().size()));
      this.repository.save(doc);
      
    } catch (FlickrException flickrException) {
      LOGGER.error("save error", flickrException);
      throw new MusicManagerException("Error when uploading image", flickrException);
    }
  }

  @Override
  public void saveMultipleMusicDocumentWithPhotos(MusicDocumentWithPhotosSaveIns ins) throws MusicManagerException {
    for (MusicDocumentWithPhotosSaveIn doc : ins.getDocs()) {
      if (ins.getPhotosDir() != null && doc.getPhotosDir() == null) {
        doc.setPhotosDir(ins.getPhotosDir());
      }
      
      if (ins.isUpload() != null && doc.getUpload() == null) {
        doc.setUpload(ins.isUpload());
      }
      
      saveMusicDocumentWithPhotos(doc);
    }
  }

  @Override
  public void saveMultipleMusicDocumentWithPhotosFromFile(File file) throws MusicManagerException {
    try {
      saveMultipleMusicDocumentWithPhotos(new ObjectMapper().readValue(file, MusicDocumentWithPhotosSaveIns.class));
    } catch (IOException e) {
      LOGGER.error("Error when save multiple doc(s)", e);
      throw new MusicManagerException("Error when save multiple doc(s)", e);
    }
  }
}