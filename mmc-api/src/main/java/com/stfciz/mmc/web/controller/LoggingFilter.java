package com.stfciz.mmc.web.controller;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class LoggingFilter extends OncePerRequestFilter {

  private static Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);
  
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      LOGGER.debug("{} >> {}", request.getRemoteAddr(), request.getRequestURL());
      filterChain.doFilter(request, response);
    } finally {
      LOGGER.debug("{} << {}", request.getRemoteAddr(), response.getStatus());
    }
  }
}