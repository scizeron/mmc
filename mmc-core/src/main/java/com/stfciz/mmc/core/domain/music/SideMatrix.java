package com.stfciz.mmc.core.domain.music;

/**
 * 
 * @author Bellevue
 *
 */
public class SideMatrix {

  @Deprecated
  private String disc;
  
  @Deprecated
  private String side;
  
  private int iSide;
  
  private int iDisc;
  
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

  public String getSide() {
    return side;
  }

  public void setSide(String side) {
    this.side = side;
  }

  public int getISide() {
    return iSide;
  }

  public void setISide(int iSide) {
    this.iSide = iSide;
  }

  public int getIDisc() {
    return iDisc;
  }

  public void setIDisc(int iDisc) {
    this.iDisc = iDisc;
  }
}
