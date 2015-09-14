package com.stfciz.mmc.core.photo.oauth;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author stfciz
 *
 */
@Aspect
@Component
public class FlickrOAuthAspect {
  
  @Autowired
  private AuthStore authStore;
  
  @Around("@annotation(flickrOauthContext)")
  public Object around(ProceedingJoinPoint pjp, FlickrOAuthContext flickrOauthContext) throws Throwable {
    this.authStore.setTheRequestContext(PermissionUtils.from(flickrOauthContext.value()));
    return pjp.proceed();
  }
}