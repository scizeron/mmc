package com.stfciz.clt.photo.oauth;

import com.flickr4java.flickr.auth.Permission;
import com.stfciz.clt.CoreConfiguration;

/**
 * 
 * @author ByTel
 *
 */
public final class PermissionUtils {

  private PermissionUtils() {
    /** EMPTY **/
  }

  public static Permission from(int value) {
    return value == Permission.DELETE_TYPE ? Permission.DELETE
         : value == Permission.WRITE_TYPE ? Permission.WRITE
         : value == Permission.READ_TYPE ? Permission.READ
         : Permission.NONE;
  }
  
  public static String getToken(CoreConfiguration appConfiguration, Permission permission) {
    return Permission.READ.equals(permission) ? appConfiguration.getFlickr().getTokens().getRead()
         : Permission.WRITE.equals(permission) ? appConfiguration.getFlickr().getTokens().getWrite()
         : Permission.DELETE.equals(permission) ? appConfiguration.getFlickr().getTokens().getDelete() : null;
  }
}