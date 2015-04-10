package com.stfciz.mmc.core.photo.domain;

/**
 * 
 * @author stfciz
 *
 */
public class Tag {
  
  private static final String TAG_SEPARATOR = "#";

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
    return this.name.name() + TAG_SEPARATOR + this.value;
  }
  
  /**
   * 
   * @param value
   * @return
   */
  public static Tag fromString(String value) {
    try {
      String[] split = value.split(TAG_SEPARATOR);
      if (split != null && split.length == 2) {
       return new Tag(TagName.valueOf(split[0]), split[1]);
      }
    } catch(Exception e) {
      /** NOOP **/
    }
    return null;
  }
}
