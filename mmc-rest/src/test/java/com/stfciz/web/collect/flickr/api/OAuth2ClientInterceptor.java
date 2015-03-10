package com.stfciz.web.collect.flickr.api;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
/**
 * 
 * @author ByTel
 *
 */
public class OAuth2ClientInterceptor implements ClientHttpRequestInterceptor {

  private AccessTokenProvider accAcessTokenProvider;

  public OAuth2ClientInterceptor(AccessTokenProvider accAcessTokenProvider) {
    this.accAcessTokenProvider = accAcessTokenProvider;
  }

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    try {
      if (!request.getURI().toASCIIString().contains("/authorize")) {
        request.getHeaders().add("Authorization", "bearer " + accAcessTokenProvider.getAccessToken());
      }
    } catch (Exception e) {
      throw new IOException(e);
    }
    return execution.execute(request, body);
  }
}
