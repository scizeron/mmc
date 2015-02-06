package com.stfciz.clt.web.oauth2;
/**
 * 
 * @author ByTel
 *
 */
public class OAuth2Context {
  
  private static ThreadLocal<OAuth2Context> context = new ThreadLocal<>();
  
  private String accessToken;
  
  private String clientId;
  
  public OAuth2Context(String accessToken, String clientId) {
    this.accessToken = accessToken;
    this.clientId = clientId;
  }
  
  public static OAuth2Context get() {
    return context.get();
  }
  
  public static void  set(OAuth2Context auth2Context) {
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
