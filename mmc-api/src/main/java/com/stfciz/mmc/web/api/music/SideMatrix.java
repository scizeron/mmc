package com.stfciz.mmc.web.api.music;

/**
 * 
 * @author Bellevue
 *
 */
public class SideMatrix {
  private String disc;
  
  private int side;
  
  private String value;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getDisc() {
    return disc;
  }

  public void setDisc(String disc) {
    this.disc = disc;
  }

  public int getSide() {
    return side;
  }

  public void setSide(int side) {
    this.side = side;
  }
}
