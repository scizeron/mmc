package com.stfciz.mmc.web.api;

import org.apache.commons.lang3.StringUtils;

import com.stfciz.mmc.core.domain.AbstractDocument;
import com.stfciz.mmc.web.api.photo.PhotoApiConverter;
/**
 * 
 * @author stfciz
 *
 * 16 juin 2015
 */
public abstract class AbstractApiConverter<D extends AbstractDocument, GR, NR, UR, FER extends AbstractBaseResponse, FR> {
  
  private PhotoApiConverter photoApiConverter;
  
  /**
   * 
   * @param photoApiConverter
   */
  public AbstractApiConverter(PhotoApiConverter photoApiConverter) {
    this.photoApiConverter = photoApiConverter;
  }
  
  /**
   * 
   * @return
   */
  public PhotoApiConverter getPhotoApiConverter() {
    return this.photoApiConverter;
  }
  
  /**
   * 
   * @param str
   * @return
   */
  public static String capitalizeWords(String str) {
    if (StringUtils.isBlank(str)) {
      return str;
    }
    StringBuilder result = new StringBuilder();
    String [] words = StringUtils.split(str);
    for (String word : words) {
      if (result.length() > 0) {
        result.append(" ");
      }
      result.append(StringUtils.capitalize(word.toLowerCase()));
    }
    return result.toString();
  }

  /**
   * 
   * @param doc
   * @return
   */
  public abstract GR convertToGetResponse(D doc);
  
  /**
   * 
   * @param request
   * @return
   */
  public abstract D convertNewRequestContent(NR request);
  
  /**
   * 
   * @param request
   * @return
   */
  public abstract D convertUpdateRequestContent(UR request);
  
  /**
   * 
   * @param doc
   * @return
   */
  public abstract FER convertToFindDocument(D doc);
  
  /**
   * 
   * @return
   */
  public abstract FR newFindResponse();
  
}
