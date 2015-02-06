package com.stfciz.clt.web.converter;

import org.springframework.stereotype.Component;

import com.flickr4java.flickr.FlickrException;

/**
 * 
 * @author ByTel
 *
 */
@Component
public class ApiConverter {

  /**
   * 
   * @param src
   * @return
   * @throws FlickrException 
   */
  public com.stfciz.clt.web.api.Photo convertPhotoDomain(com.flickr4java.flickr.photos.Photo src) throws FlickrException {
    com.stfciz.clt.web.api.Photo target = new com.stfciz.clt.web.api.Photo();
    target.setId(src.getId());
    target.putUrl("t", src.getThumbnailUrl());
    target.putUrl("o", src.getOriginalUrl());
    target.putUrl("m", src.getMediumUrl());
    return target;
  }
}
