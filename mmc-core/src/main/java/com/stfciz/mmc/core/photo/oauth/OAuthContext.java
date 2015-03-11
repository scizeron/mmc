package com.stfciz.mmc.core.photo.oauth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.flickr4java.flickr.auth.Permission;

/**
 * 
 * @author stfciz
 *
 */
@Target(value={ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OAuthContext {

  /**
   * 
   * @return
   */
  int value() default Permission.READ_TYPE;
  
}
