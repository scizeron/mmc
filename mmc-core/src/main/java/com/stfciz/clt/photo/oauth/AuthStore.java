package com.stfciz.clt.photo.oauth;

import com.flickr4java.flickr.auth.Permission;
/**
 * 
 * @author ByTel
 *
 */
public interface AuthStore {

  void setTheRequestContext(Permission permission);
  
  void releaseTheRequestContext();
}