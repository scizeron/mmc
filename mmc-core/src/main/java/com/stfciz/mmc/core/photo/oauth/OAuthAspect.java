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
public class OAuthAspect {
  
  @Autowired
  private AuthStore authStore;
  
  @Around("@annotation(oauthContext)")
  public Object around(ProceedingJoinPoint pjp, OAuthContext oauthContext) throws Throwable {
    this.authStore.setTheRequestContext(PermissionUtils.from(oauthContext.value()));
    return pjp.proceed();
  }
}