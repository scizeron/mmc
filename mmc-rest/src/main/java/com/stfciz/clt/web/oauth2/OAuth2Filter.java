package com.stfciz.clt.web.oauth2;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 
 * @author stfciz
 *
 */
@Component
public class OAuth2Filter extends OncePerRequestFilter {

  private static final String ACCESS_TOKEN_PARAMETER_NAME = "access_token";

  private static final String AUTHORIZATION_HEADER_NAME   = "Authorization";

  private static final Logger LOGGER                      = LoggerFactory.getLogger(OAuth2Filter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String clientId = request.getParameter("client_id");
    String authorization = request.getHeader(AUTHORIZATION_HEADER_NAME);
    String accessToken = null;
    
    if (StringUtils.isNotBlank(authorization)) {
      accessToken = authorization.substring(7); // 'Bearer '
      LOGGER.debug("Header[{}] : {}", new Object[] { AUTHORIZATION_HEADER_NAME, accessToken });
    } else {
      accessToken = request.getParameter(ACCESS_TOKEN_PARAMETER_NAME);
      LOGGER.debug("Parameter[{}] : {}", new Object[] { ACCESS_TOKEN_PARAMETER_NAME, accessToken });
    }
    
    try {
      OAuth2Context.set(new OAuth2Context(accessToken, clientId));
      filterChain.doFilter(request, response);
    } finally {
      OAuth2Context.set(null);
    }
  }
}