package com.stfciz.mmc.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.stfciz.mmc.core.CoreConfiguration;
import com.stfciz.mmc.web.api.photo.PhotoApiConverter;
/**
 * 
 * @author Bellevue
 *
 */
public abstract class AbstractApiController {

  protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
  
  @Autowired
  protected CoreConfiguration configuration;
  
  @Autowired
  protected PhotoApiConverter photoApiConverter;
}
