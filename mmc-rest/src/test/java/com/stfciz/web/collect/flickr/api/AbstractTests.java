package com.stfciz.web.collect.flickr.api;

import java.net.URI;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.impl.client.HttpClients;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.flickr4java.flickr.Flickr;
import com.stfciz.clt.AppSpringCoreConfiguration;
import com.stfciz.clt.web.AppSpringWebConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { AppSpringCoreConfiguration.class, AppSpringWebConfiguration.class}
  , initializers= {ConfigFileApplicationContextInitializer.class}
)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public abstract class AbstractTests implements AcessTokenProvider {

  protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTests.class);
  
  @Value("${local.server.port}")
  protected int            port;
  
  protected RestTemplate  restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HttpClients.custom().disableRedirectHandling().build()));
  
  public AbstractTests() {
    this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(HttpClients.custom().disableRedirectHandling().build()));
    this.restTemplate.getInterceptors().add(new OAuth2ClientInterceptor(this));
  }
  
  @BeforeClass
  public static void staticInit() {
    Flickr.debugRequest = true;
    Flickr.debugStream = true;
  }
  
  public static String getAuthorizationHeaderValue(String accessToken) throws Exception {
    return "bearer " + new String(Base64.encodeBase64(accessToken.getBytes("UTF-8")), "UTF-8");
  }
   
  /**
   * 
   * @param oauth2Host
   * @param oauth2Port
   * @return
   * @throws Exception
   */
  protected String newAccessToken(String oauth2Host, int oauth2Port) throws Exception {
    final String state = UUID.randomUUID().toString();
    final String clientId = "test";
    final String scope = "read";
    final String redirectUri = "http://localhost/sp.html";
    final String responseType = "token";
    
    final URI uri = URI.create(String.format("http://%s:%d/authz/authorize?client_id=%s&scope=%s&state=%s&response_type=%s&redirect_uri=%s", oauth2Host, oauth2Port,
        clientId, URLEncoder.encode(scope, "UTF-8"), state, responseType, redirectUri));
    
    ResponseEntity<String> response = this.restTemplate.getForEntity(uri, String.class);
    return extractAccessToken(response.getHeaders().getLocation().toASCIIString());
  }

  /**
   * 
   * @param response
   * @return
   */
  protected String extractAccessToken(String response) {
    Matcher matcher = Pattern.compile("(.*#access_token=)([a-z0-9\\-]*)").matcher(response);
    Assert.assertTrue(matcher.matches());
    String acessToken = matcher.group(2);
    LOGGER.debug("extractAccessToken: {}", response, acessToken);
    return acessToken;
  }
}
