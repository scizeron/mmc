package com.stfciz.mmc.core.domain.music;

/**
 * 
 * @author Bellevue
 *
 */
public class SideMatrix {

  private int iSide;
  
  private int iDisc;
  
  private String value;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
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
