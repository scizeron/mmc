package com.stfciz.mmc.web.api.photo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.Size;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.stfciz.mmc.core.photo.domain.ImageComparator;
import com.stfciz.mmc.core.photo.domain.PhotoDocument;
import com.stfciz.mmc.core.photo.domain.PhotoDocumentSize;
import com.stfciz.mmc.core.photo.domain.TagName;

/**
 * 
 * @author stfciz
 *
 */
@Component
public class PhotoApiConverter {

  /**
   * 
   * @author stfciz
   *
   */
  static class PhotoFeatures {
    
    private String urlMethod;
    
    private String sizeMethod;
    
    public PhotoFeatures(String urlMethod, String sizeMethod) {
      this.urlMethod = urlMethod;
      this.sizeMethod = sizeMethod;
    }
    
    public String getUrlMethod() {
      return this.urlMethod;
    }
    
    public String getSizeMethod() {
      return this.sizeMethod;
    }
  }

  /**
   * 
   */
  private final Map<String,PhotoFeatures> PHOTO_FEATURES_RETRIEVERS = ImmutableMap.of(
       "t", new PhotoFeatures("getThumbnailUrl", "getThumbnailSize"),
       "o", new PhotoFeatures("getOriginalUrl", "getOriginalSize"),
       "m", new PhotoFeatures("getMediumUrl", "getMediumSize")
      );
  
  /**
   * 
   * @param src
   * @return
   * @throws FlickrException 
   */
  public com.stfciz.mmc.web.api.photo.Photo convertPhotoDocument(PhotoDocument photoMusicDocument) {
    com.stfciz.mmc.web.api.photo.Photo target = new com.stfciz.mmc.web.api.photo.Photo();
    target.setId(photoMusicDocument.getId());
    for (Map.Entry<String, PhotoFeatures> photoFeatures : PHOTO_FEATURES_RETRIEVERS.entrySet()) {
      com.stfciz.mmc.web.api.photo.Photo.PhotoDetails photoDetails = new com.stfciz.mmc.web.api.photo.Photo.PhotoDetails();
      target.getDetails().put(photoFeatures.getKey(), photoDetails);
      photoDetails.setType(photoFeatures.getKey());
      
      if (photoMusicDocument.getSizes() != null && photoMusicDocument.getSizes().get(photoFeatures.getKey()) != null) {
        photoDetails.setHeight(photoMusicDocument.getSizes().get(photoFeatures.getKey()).getHeight());
        photoDetails.setWidth(photoMusicDocument.getSizes().get(photoFeatures.getKey()).getWidth());
      }
      
      Photo photo = new Photo();
      photo.setId(photoMusicDocument.getId());
      photo.setServer(photoMusicDocument.getServerId());
      photo.setFarm(photoMusicDocument.getFarmId());
      photo.setSecret(photoMusicDocument.getSecret());
      photo.setOriginalSecret(photoMusicDocument.getOrginalSecret());
      photoDetails.setUrl((String)ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(photo.getClass(), photoFeatures.getValue().getUrlMethod()), photo));
    }
    
    return target;
  }
  
  /**
   * 
   * @param src
   * @return
   */
  public com.stfciz.mmc.web.api.photo.Photo convertFlickrPhotoToApiPhoto(com.flickr4java.flickr.photos.Photo src) {
    com.stfciz.mmc.web.api.photo.Photo target = new com.stfciz.mmc.web.api.photo.Photo();
    target.setId(src.getId());
    for (Map.Entry<String, PhotoFeatures> photoFeatures : PHOTO_FEATURES_RETRIEVERS.entrySet()) {
      com.stfciz.mmc.web.api.photo.Photo.PhotoDetails photoDetails = new com.stfciz.mmc.web.api.photo.Photo.PhotoDetails();
      photoDetails.setType(photoFeatures.getKey());
      target.getDetails().put(photoFeatures.getKey(), photoDetails);
      photoDetails.setUrl((String)ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(src.getClass(), photoFeatures.getValue().getUrlMethod()), src));
      Size size = (Size) ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(src.getClass(), photoFeatures.getValue().getSizeMethod()), src);
      if (size != null) { 
        photoDetails.setHeight(size.getHeight());
        photoDetails.setWidth(size.getWidth());
      }
    }
  
    if (src.getTags() != null && !src.getTags().isEmpty()) {
        Collection<com.stfciz.mmc.core.photo.domain.Tag> docId = 
            Collections2.filter(
                Collections2.filter(
                    Collections2.transform(src.getTags(), new Function<com.flickr4java.flickr.tags.Tag, com.stfciz.mmc.core.photo.domain.Tag>() {
                      @Override
                      public com.stfciz.mmc.core.photo.domain.Tag apply(com.flickr4java.flickr.tags.Tag tag) {
                        if (tag == null) {
                          return null;
                        }
                        return com.stfciz.mmc.core.photo.domain.Tag.fromFlickrFormat(tag.getValue());
                      }
                    }), 
                    new Predicate<com.stfciz.mmc.core.photo.domain.Tag>() {
                      @Override
                      public boolean apply(com.stfciz.mmc.core.photo.domain.Tag tag) {
                        return tag != null && TagName.DOC_ID.equals(tag.getName());
                      }
                   }
                ), 
                Predicates.notNull()
            );
        
        if (docId != null && docId.size() > 0) {
          target.setDocId(docId.iterator().next().getValue());
        }
    }
    return target;
  }

  
  /**
   * 
   * @param src
   * @return
   */
  public PhotoDocument convertToPhotoMusicDocument(com.flickr4java.flickr.photos.Photo src, int order) {
    PhotoDocument target = new PhotoDocument();
    target.setId(src.getId());
    target.setFarmId(src.getFarm());
    target.setServerId(src.getServer());
    target.setOrginalSecret(src.getOriginalSecret());
    target.setSecret(src.getSecret());
    target.setOrder(order);
    for (Map.Entry<String, PhotoFeatures> photoFeatures : PHOTO_FEATURES_RETRIEVERS.entrySet()) {
      Size size = (Size) ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(src.getClass(), photoFeatures.getValue().getSizeMethod()), src);
      if (size != null) {
        PhotoDocumentSize photoMusicDocumentSize = new PhotoDocumentSize();
        photoMusicDocumentSize.setHeight(size.getHeight());
        photoMusicDocumentSize.setWidth(size.getWidth());
        target.getSizes().put(photoFeatures.getKey(), photoMusicDocumentSize);
      }
    }
    
    return target;
  }
  
  /**
   * 
   * @param photoMusicDocuments
   * @return
   * @throws FlickrException
   */
  public List<com.stfciz.mmc.web.api.photo.Photo> convertToApiPhotos(List<PhotoDocument> photoMusicDocuments) {
    if (photoMusicDocuments == null || photoMusicDocuments.isEmpty()) {
      return null;
    }
    
    List<com.stfciz.mmc.web.api.photo.Photo> target = new ArrayList<com.stfciz.mmc.web.api.photo.Photo>();
    for (PhotoDocument photoMusicDocument : photoMusicDocuments) {
      target.add(convertPhotoDocument(photoMusicDocument));
    }
    return target;
  }
  
  /**
   * 
   * @return
   */
  public List<PhotoDocument> getSortedImages(List<PhotoDocument> imgs) {
    if (imgs != null) {
      Collections.sort(imgs, ImageComparator.get());
    }
    return imgs;
  }
}
