package com.stfciz.mmc.web.oauth2;

/**
 * 
 * @author stfciz
 *
 */
public class OAuth2Context {
  
  private static ThreadLocal<OAuth2Context> context = new ThreadLocal<>();
  
  private String accessToken;
  
  private String clientId;
  
  /**
   * 
   * @param accessToken
   * @param clientId
   */
  public OAuth2Context(String accessToken, String clientId) {
    this.accessToken = accessToken;
    this.clientId = clientId;
  }
  
  /**
   * 
   * @return
   */
  public static OAuth2Context get() {
    return context.get();
  }
  
  /**
   * 
   * @param auth2Context
   */
  public static void set(OAuth2Context auth2Context) {
    context.set(auth2Context);
  }

  /**
   * @return the accessToken
   */
  public String getAccessToken() {
    return this.accessToken;
  }

  /**
   * @return the clientId
   */
  public String getClientId() {
    return this.clientId;
  }

}
