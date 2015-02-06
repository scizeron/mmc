package com.stfciz.clt.photo;

import javax.validation.constraints.NotNull;


/**
 * 
 * @author ByTel
 *
 */
public class FlickrConfiguration {
  
  public static class Tokens {
    private String read;
    
    private String write;

    private String delete;
    
    private String secretCode;

    /**
     * @return the read
     */
    public String getRead() {
      return this.read;
    }

    /**
     * @param read the read to set
     */
    public void setRead(String read) {
      this.read = read;
    }

    /**
     * @return the write
     */
    public String getWrite() {
      return this.write;
    }

    /**
     * @param write the write to set
     */
    public void setWrite(String write) {
      this.write = write;
    }

    /**
     * @return the delete
     */
    public String getDelete() {
      return this.delete;
    }

    /**
     * @param delete the delete to set
     */
    public void setDelete(String delete) {
      this.delete = delete;
    }

    /**
     * @return the secretCode
     */
    public String getSecretCode() {
      return this.secretCode;
    }

    /**
     * @param secretCode the secretCode to set
     */
    public void setSecretCode(String secretCode) {
      this.secretCode = secretCode;
    }
  }
  
  /**
   * 
   * @author ByTel
   *
   */
  public static class User {
    @NotNull
    private String id;
    
    @NotNull
    private String apiKey;
    
    @NotNull
    private String secretCode;

    /**
     * @return the id
     */
    public String getId() {
      return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
      this.id = id;
    }
    
    /**
     * @return the apiKey
     */
    public String getApiKey() {
      return this.apiKey;
    }

    /**
     * @param apiKey the apiKey to set
     */
    public void setApiKey(String apiKey) {
      this.apiKey = apiKey;
    }

    /**
     * @return the secretCode
     */
    public String getSecretCode() {
      return this.secretCode;
    }

    /**
     * @param secretCode the secretCode to set
     */
    public void setSecretCode(String secretCode) {
      this.secretCode = secretCode;
    }
  }
  
  @NotNull
  private User user;
  
  @NotNull
  private Tokens tokens;
  
  @NotNull
  private String appGalleryId;
  
  private String proxy;
  
  /**
   * 
   * @param user
   */
  public void setUser(User user) {
    this.user = user;
  }



  /**
   * @return the user
   */
  public User getUser() {
    return this.user;
  }



  /**
   * @return the tokens
   */
  public Tokens getTokens() {
    return this.tokens;
  }



  /**
   * @param tokens the tokens to set
   */
  public void setTokens(Tokens tokens) {
    this.tokens = tokens;
  }



  /**
   * @return the appGalleryId
   */
  public String getAppGalleryId() {
    return this.appGalleryId;
  }



  /**
   * @param appGalleryId the appGalleryId to set
   */
  public void setAppGalleryId(String appGalleryId) {
    this.appGalleryId = appGalleryId;
  }



  /**
   * @return the proxy
   */
  public String getProxy() {
    return this.proxy;
  }



  /**
   * @param proxy the proxy to set
   */
  public void setProxy(String proxy) {
    this.proxy = proxy;
  }
}
