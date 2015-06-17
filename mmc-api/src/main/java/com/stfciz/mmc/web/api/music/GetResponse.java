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
}
