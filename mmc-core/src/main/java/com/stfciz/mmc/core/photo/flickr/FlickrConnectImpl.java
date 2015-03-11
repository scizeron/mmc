package com.stfciz.mmc.core.photo.flickr;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.stfciz.mmc.core.CoreConfiguration;

/**
 * 
 * @author stfciz
 *
 */
@Component
public class FlickrConnectImpl implements FlickrConnect {

  private static final Logger     LOGGER = LoggerFactory.getLogger(FlickrConnect.class);

  private CoreConfiguration configuration;

  private REST             rest = null;

  @Autowired
  public FlickrConnectImpl(CoreConfiguration configuration) {
    this.configuration = configuration;
  }

  @PostConstruct
  private REST getRest() {
    if (this.rest == null) {
      synchronized (FlickrConnectImpl.class) {
        if (this.rest == null) {
          this.rest = new REST();
          if (StringUtils.isNotBlank(this.configuration.getFlickr().getProxy())) {
            LOGGER.debug("Flickr connection with proxy : {}", this.configuration.getFlickr().getProxy());
            String[] proxyParts = this.configuration.getFlickr().getProxy().split(":");
            String proxyHost = proxyParts[0];
            int proxyPort = Integer.parseInt(proxyParts[1]);
            rest.setProxy(proxyHost, proxyPort);
            // bug in the REST:setProxy method
            System.setProperty("https.proxyHost", proxyHost);
            System.setProperty("https.proxyPort", String.valueOf(proxyPort));
          }
        }
      }
    }
    return this.rest;
  }

  @Override
  public Flickr getFlickr() {
    return new Flickr(this.configuration.getFlickr().getUser().getApiKey(), this.configuration.getFlickr().getUser().getSecretCode(), getRest());
  }

  @Override
  public String getUserId() {
    return this.configuration.getFlickr().getUser().getId();
  }
}
