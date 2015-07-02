package com.stfciz.mmc.web.api.music;

/**
 * 
 * @author Bellevue
 *
 */
public class SideMatrix {
 
  private int disc;
  
  private int side;
  
  private String value;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public int getDisc() {
    return disc;
  }

  public void setDisc(int disc) {
    this.disc = disc;
  }

  public int getSide() {
    return side;
  }

  public void setSide(int side) {
    this.side = side;
  }
}
