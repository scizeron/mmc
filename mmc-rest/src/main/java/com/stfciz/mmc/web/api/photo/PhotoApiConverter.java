package com.stfciz.mmc.web.api.photo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.tags.Tag;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.stfciz.mmc.core.music.ImageComparator;
import com.stfciz.mmc.core.music.domain.PhotoMusicDocument;

/**
 * 
 * @author stfciz
 *
 */
@Component
public class PhotoApiConverter {

  private static final Logger LOGGER = LoggerFactory.getLogger(PhotoApiConverter.class);
  
  private final ObjectMapper  tagMapper = new ObjectMapper();
  /**
   * 
   * @param src
   * @return
   * @throws FlickrException 
   */
  public com.stfciz.mmc.web.api.photo.Photo convertPhotoDomain(com.flickr4java.flickr.photos.Photo src) throws FlickrException {
    com.stfciz.mmc.web.api.photo.Photo target = new com.stfciz.mmc.web.api.photo.Photo();
    target.setId(src.getId());
    target.putUrl("t", src.getThumbnailUrl());
    target.putUrl("o", src.getOriginalUrl());
    target.putUrl("m", src.getMediumUrl());
    if (src.getTags() != null && !src.getTags().isEmpty()) {
      target.setMusicDocIds(new ArrayList<String>(
        Collections2.filter(Collections2.transform(
          Collections2.transform(src.getTags(), new Function<Tag, com.stfciz.mmc.core.photo.domain.Tag>() {
            @Override
            public com.stfciz.mmc.core.photo.domain.Tag apply(Tag tag) {
              try {
                return tagMapper.readValue(tag.getValue(),com.stfciz.mmc.core.photo.domain.Tag.class);
              } catch (IOException e) {
                LOGGER.error("read tag error", e);
                return null;
              }
            }
          }), new Function<com.stfciz.mmc.core.photo.domain.Tag, String>() {
                @Override
                public String apply(com.stfciz.mmc.core.photo.domain.Tag tag) {
                  return "musicDoc".equals(tag.getName()) ? tag.getValue() : null;
                }
        }), Predicates.isNull())));
    }
    return target;
  }
  
  /**
   * 
   * @param src
   * @return
   */
  public PhotoMusicDocument convertToPhotoMusicDocument(com.flickr4java.flickr.photos.Photo src, int order) {
    PhotoMusicDocument target = new PhotoMusicDocument();
    target.setId(src.getId());
    target.setFarmId(src.getFarm());
    target.setServerId(src.getServer());
    target.setOrginalSecret(src.getOriginalSecret());
    target.setSecret(src.getSecret());
    target.setOrder(order);
    return target;
  }
  
  /**
   * 
   * @return
   */
  public List<PhotoMusicDocument> getSortedImages(List<PhotoMusicDocument> imgs) {
    if (imgs != null) {
      Collections.sort(imgs, ImageComparator.get());
    }
    return imgs;
  }
}
