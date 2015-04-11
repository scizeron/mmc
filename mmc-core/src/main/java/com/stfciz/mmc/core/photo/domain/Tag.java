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
  
  @Override
  public String toString() {
    return this.name.getValue() + this.value;
  }
  
  /**
   * 
   * @param value
   * @return
   */
  public static Tag fromString(String value) {
    try {
      TagName[] tagNames = TagName.values();
      for (TagName tagName : tagNames) {
        if (value.startsWith(tagName.getValue())) {
          return new Tag(tagName, StringUtils.remove(value, tagName.getValue()));
        }
      }
    } catch(Exception e) {
      /** NOOP **/
    }
    return null;
  }
}
