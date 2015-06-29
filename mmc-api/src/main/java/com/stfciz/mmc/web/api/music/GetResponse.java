package com.stfciz.mmc.web.api.music;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author stfciz
 *
 */
public class GetResponse extends AbstractMusicBaseResponse {

  private List<SideMatrix> sideMatrixes;
  
  private List<Song> songs;
  
  /**
   * 
   * @return
   */
  public List<SideMatrix> getSideMatrixes() {
    if (this.sideMatrixes == null) {
      this.sideMatrixes = new ArrayList<>();
    }
    return this.sideMatrixes;
  }
  
  /**
   * 
   * @param sidesMatrix
   */
  public void setSideMatrixes(List<SideMatrix> sideMatrixes) {
    this.sideMatrixes = sideMatrixes;
  }
  
  /**
   * 
   * @return
   */
  public List<Song> getSongs() {
    if (this.songs == null) {
      this.songs = new ArrayList<>();
    }
    return this.songs;
  }

  /**
   * 
   * @param songs
   */
  public void setSongs(List<Song> songs) {
    this.songs = songs;
  }
}
