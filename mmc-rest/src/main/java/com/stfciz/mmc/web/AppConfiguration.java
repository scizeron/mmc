package com.stfciz.mmc.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @author stfciz
 *
 */
@Component
@ConfigurationProperties(prefix = "web")
public class AppConfiguration {

  private String oauth2VerifyUri;
  
  private String esDirectory;

  /**
   * @return the oauth2VerifyUri
   */
  public String getOauth2VerifyUri() {
    return this.oauth2VerifyUri;
  }

  /**
   * @param oauth2VerifyUri the oauth2VerifyUri to set
   */
  public void setOauth2VerifyUri(String oauth2VerifyUri) {
    this.oauth2VerifyUri = oauth2VerifyUri;
  }

  /**
   * 
   * @return
   */
  public String getEsDirectory() {
    return esDirectory;
  }

  /**
   * 
   * @param esDirectory
   */
  public void setEsDirectory(String esDirectory) {
    this.esDirectory = esDirectory;
  }
}
