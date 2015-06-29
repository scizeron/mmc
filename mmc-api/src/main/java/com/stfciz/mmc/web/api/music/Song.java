package com.stfciz.mmc.web.api.music;

/**
 * 
 * @author stfciz
 *
 * 29 juin 2015
 */
public class Song {
  
  private String title;
  
  private int order;
  
  private int mins;
  
  private int secs;
  
  private int side;
  
  private int disc;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  public int getMins() {
    return mins;
  }

  public void setMins(int mins) {
    this.mins = mins;
  }

  public int getSecs() {
    return secs;
  }

  public void setSecs(int secs) {
    this.secs = secs;
  }

  public int getSide() {
    return side;
  }

  public void setSide(int side) {
    this.side = side;
  }

  public int getDisc() {
    return disc;
  }

  public void setDisc(int disc) {
    this.disc = disc;
  }

}
