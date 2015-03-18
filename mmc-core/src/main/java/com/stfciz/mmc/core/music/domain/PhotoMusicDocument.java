package com.stfciz.mmc.core.music.domain;

import java.util.HashMap;
import java.util.Map;


/**
 * {
 *  "urls":{"t":"https://farm<id>.staticflickr.com/<server>/<id>_<secret>_t.jpg"
 *         ,"o":"https://farm<id>.staticflickr.com/<server>/<id>_<original_secret>_o.jpg"
 *         ,"m":"https://farm<id>.staticflickr.com/<server>/<id>_<secret>.jpg"
 * }}
 * @author stfciz
 *
 */
public class PhotoMusicDocument {

  private String id;
  
  private String secret;
  
  private String orginalSecret;
  
  private String farmId;
  
  private String serverId;
  
  private Integer order;
  
  private boolean main;
  
  private Map<String,PhotoMusicDocumentSize> sizes = new HashMap<>();

  /**
   * 
   */
  public PhotoMusicDocument() {
    /** EMPTY **/
  }


  /**
   * @return the order
   */
  public Integer getOrder() {
    return this.order;
  }

  /**
   * @param order the order to set
   */
  public void setOrder(Integer order) {
    this.order = order;
  }


  public String getOrginalSecret() {
    return this.orginalSecret;
  }


  public void setOrginalSecret(String orginalSecret) {
    this.orginalSecret = orginalSecret;
  }


  public String getFarmId() {
    return this.farmId;
  }


  public void setFarmId(String farmId) {
    this.farmId = farmId;
  }


  public String getServerId() {
    return this.serverId;
  }


  public void setServerId(String serverId) {
    this.serverId = serverId;
  }


  public String getId() {
    return this.id;
  }


  public void setId(String id) {
    this.id = id;
  }


  public String getSecret() {
    return this.secret;
  }


  public void setSecret(String secret) {
    this.secret = secret;
  }

  public boolean isMain() {
    return this.main;
  }


  public void setMain(boolean main) {
    this.main = main;
  }


  public Map<String, PhotoMusicDocumentSize> getSizes() {
    return this.sizes;
  }


  public void setSizes(Map<String, PhotoMusicDocumentSize> sizes) {
    this.sizes = sizes;
  }
}
