package com.stfciz.mmc.web.oauth2;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stfciz.mmc.web.AppConfiguration;

/**
 * 
 * @author stfciz
 *
 */
@Aspect
public class PermissionAspect {

  private static final Logger LOGGER       = LoggerFactory.getLogger(PermissionAspect.class);

  private RestTemplate        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

  @Autowired
  private AppConfiguration appConfiguration;
  
  private PermissionValidator permissionValidator = new PermissionValidator();

  @Around("@annotation(permission)")
  public Object around(ProceedingJoinPoint pjp, Permission permission) throws Throwable {
    String accessToken = OAuth2Context.get().getAccessToken();
    String clientId = OAuth2Context.get().getClientId();
    
    if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(clientId)) {
      throw new PermissionException("Missing parameters : client_id and/or access_token", 401);
    }
    
    String method = ((MethodSignature)pjp.getSignature()).getMethod().getName();
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
    form.set("client_id", clientId);
    form.set("access_token", accessToken);
    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(form, requestHeaders);

    LOGGER.debug("Verify accessToken: {}, client_id: {} on {}", new Object[] { accessToken, clientId, this.appConfiguration.getOauth2VerifyUri() });
    ResponseEntity<VerifyOut> response = null;
    
    try {
      response = this.restTemplate.exchange(this.appConfiguration.getOauth2VerifyUri(), HttpMethod.POST, requestEntity, VerifyOut.class);
    } catch(HttpClientErrorException e) {
      LOGGER.error("Verify accessToken: {}, client_id: {} on {} is failed, status: {}", new Object[] { accessToken, clientId, this.appConfiguration.getOauth2VerifyUri(), e.getStatusText()});
      throw new PermissionException("Granted scopes check failed", 403);
    }
    
    String[] grantedScopes = response.getBody().getGrantedScopes();
    String[] userRoles = response.getBody().getUserRoles();
    
    LOGGER.debug("grantedScopes: {}, userRoles: {} for {}", Arrays.toString(grantedScopes), Arrays.toString(userRoles), accessToken);

    //////////////////////////////////////////////////////////////////////////
    // verify
    ////////////////////////////////////////////////////////////////////////
    
    if (!this.permissionValidator.validScopes(grantedScopes, permission.scopes())) {
      LOGGER.error("OAuth2 verify failed, {} scopes not granted for \"{}\".", grantedScopes, accessToken);
      throw new PermissionException("Insufficient granted scopes", 403);
    }
    
    if (!this.permissionValidator.validRoles(userRoles, permission.roles())) {
      LOGGER.error("OAuth2 verify failed for {}, {} roles not allowed for \"{}\".", method, userRoles, accessToken);
      throw new PermissionException("Insufficient roles", 403);
    }
    
    LOGGER.debug("OAuth2 verify OK for {} - \"{}\".", method, accessToken);
    return pjp.proceed();
  }

  /**
   * 
   * @author ByTel
   * 
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class VerifyOut {
    private String[] grantedScopes;
    
    private String[] userRoles;

    /**
     * @return the grantedScopes
     */
    public String[] getGrantedScopes() {
      return this.grantedScopes;
    }

    /**
     * @return the userRoles
     */
    public String[] getUserRoles() {
      return this.userRoles;
    }
  }
}
