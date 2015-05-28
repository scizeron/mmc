package com.stfciz.mmc.web.controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class LoggingFilter extends OncePerRequestFilter {

  private static Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);
  
  private static final String REQUEST_ID_MDC_ATTR = "REQ_ID";
  
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      MDC.put(REQUEST_ID_MDC_ATTR, UUID.randomUUID().toString());
      LOGGER.debug("remoteAddr:{},secure:{}  >> {}", new Object[]{request.getRemoteAddr(), request.isSecure(), request.getRequestURI()});
      filterChain.doFilter(request, response);
    } finally {
      LOGGER.debug("remoteAddr:{},secure:{} << {}", new Object[]{request.getRemoteAddr(), request.isSecure(), response.getStatus()});
      MDC.remove(REQUEST_ID_MDC_ATTR);
    }
  }
}