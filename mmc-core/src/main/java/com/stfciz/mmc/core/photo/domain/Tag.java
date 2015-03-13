package com.stfciz.mmc.core.photo.domain;

/**
 * 
 * @author stfciz
 *
 */
public class Tag {
  
  /**
   * 
   */
  public Tag() {}

  /**
   * 
   * @param name
   * @param value
   */
  public Tag(String name, String value) {
    super();
    this.name = name;
    this.value = value;
  }

  private String name;
  
  private String value;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
