package com.stfciz.mmc.core.photo.oauth;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.stfciz.mmc.core.CoreConfiguration;
import com.stfciz.mmc.core.photo.flickr.FlickrConnect;

/**
 * 
 * @author stfciz
 *
 */
@Component
public class InMemoryAuthStore implements AuthStore {
  
  @Autowired
  private FlickrConnect flickrConnect;

  private Map<String, Auth> authents = new HashMap<>();
    
  @Autowired
  private CoreConfiguration appConfiguration;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryAuthStore.class);

  @Override
  public void releaseTheRequestContext() {
    RequestContext.getRequestContext().setAuth(null);
  }

  @Override
  public void setTheRequestContext(Permission permission) {
    RequestContext requestContext = RequestContext.getRequestContext();
    Auth auth = requestContext.getAuth();
    if (auth == null || (auth != null && !auth.getPermission().equals(permission))) {
      LOGGER.debug("The current auth is \"{}\".", ToStringBuilder.reflectionToString(auth));
      auth = this.authents.get(permission.toString());
      if (auth == null) {
        LOGGER.debug("No auth in the store for '{}' permission. Create a new one.", permission);
        auth = new Auth();
        auth.setPermission(permission);
        auth.setToken(PermissionUtils.getToken(this.appConfiguration, permission));
        auth.setTokenSecret(this.appConfiguration.getFlickr().getTokens().getSecretCode());
        LOGGER.debug("New auth \"{}\".", ToStringBuilder.reflectionToString(auth));
        this.authents.put(auth.getPermission().toString(),auth); 
      }
      requestContext.setAuth(auth);
    } else {
      LOGGER.debug("The \"{}\" permission is already present in the current thread.", permission);
    }
  }
}
