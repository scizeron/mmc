package com.stfciz.mmc.core.photo.domain;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author stfciz
 *
 */
public class Tag {
  
  private TagName name;
  
  private String value;

  /**
   * 
   * @param name
   * @param value
   */
  public Tag(TagName name, String value) {
    this.name = name;
    this.value = value;
  }

  public TagName getName() {
    return this.name;
  }

  public void setName(TagName name) {
    this.name = name;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }
  
  /**
   * flickr.photos.getInfo retourne le tag en lowercase se qui fausse le truc
   * alors qu'il est ok pourtant (mix maj/min)
   * 
   * @return
   */
  public String toFlickrFormat() {
    StringBuilder result = new StringBuilder(this.name.getValue());
    this.value.chars().forEach(c->result.append("c"+c));
    return result.toString();
  }
  
  /**
   * 
   * @param flickrValue
   * @return
   */
  public static Tag fromFlickrFormat(String flickrValue) {
    try {
      TagName[] tagNames = TagName.values();
      for (TagName tagName : tagNames) {
        if (flickrValue.startsWith(tagName.getValue())) {
         String [] cValues = StringUtils.remove(flickrValue, tagName.getValue()).split("c");
         StringBuilder tagValue = new StringBuilder();
         for (String cValue : cValues) {
           if (StringUtils.isNotEmpty(cValue)) {
             tagValue.append((char)Integer.parseInt(cValue));
           }
         }
         return new Tag(tagName, tagValue.toString());
        }
      }
    } catch(Exception e) {
      /** NOOP **/
    }
    return null;
  }
}
