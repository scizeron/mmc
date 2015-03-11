package com.stfciz.mmc.core.photo.oauth;

import com.flickr4java.flickr.auth.Permission;

/**
 * 
 * @author stfciz
 *
 */
public interface AuthStore {

  void setTheRequestContext(Permission permission);
  
  void releaseTheRequestContext();
}