package com.stfciz.mmc.core.music.domain;

/**
 * 
 * @author stfciz
 *
 */
public class Obi {
  /**
   * 
   * @author stfciz
   *
   */
  public enum Orientation {
    HORIZONTAL("H"), VERTICAL("V");
    private String value;
    private Orientation(String value) {
      this.value = value;
    }
    public String getValue() {
      return this.value;
    }
  }

  private String color;
  
  private Orientation orientation;

  public Obi() {
    
  }
  
  public Obi(String color, Orientation orientation) {
    this.color = color;
    this.orientation = orientation;
  }
  
  public String getColor() {
    return this.color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public Orientation getOrientation() {
    return this.orientation;
  }

  public void setOrientation(Orientation orientation) {
    this.orientation = orientation;
  }
}
