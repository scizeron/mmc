package com.stfciz.mmc.core.photo.flickr;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;

/**
 * 
 * @author stfciz
 *
 * 18 juin 2015
 */
public class FlickrConfigurationTests {

  /**
   * 
   */
  @Test public void loading() throws Exception {
    PropertySource<?> configuration = new YamlPropertySourceLoader().load("configuration", new FileSystemResource("src/test/resources/configuration.yml"), null);
    Assert.assertThat(configuration, CoreMatchers.notNullValue());
    //configuration.getSource()
    FlickrConfiguration target = new  FlickrConfiguration();
    PropertiesConfigurationFactory<Object> factory = new PropertiesConfigurationFactory<Object>(target);
    MutablePropertySources propertySources = new MutablePropertySources();
    propertySources.addFirst(configuration);
    factory.setPropertySources(propertySources);
    factory.setIgnoreInvalidFields(false);
    factory.setIgnoreNestedProperties(false);
    factory.setIgnoreNestedProperties(false);
    factory.setExceptionIfInvalid(true);
    factory.bindPropertiesToTarget();
    Assert.assertThat(target.getGalleries().size(), CoreMatchers.is(3));
    Assert.assertThat(target.getGalleryId("book"), CoreMatchers.is("123"));
    Assert.assertThat(target.getGalleryId("music"), CoreMatchers.is("456"));
    Assert.assertThat(target.getGalleryId("misc"), CoreMatchers.is("789"));
    Assert.assertThat(target.getTokens().getRead(), CoreMatchers.is("34"));
    Assert.assertThat(target.getTokens().getWrite(), CoreMatchers.is("56"));
    Assert.assertThat(target.getTokens().getDelete(), CoreMatchers.is("78"));
    Assert.assertThat(target.getTokens().getSecretCode(), CoreMatchers.is("12"));
    Assert.assertThat(target.getUser().getApiKey(), CoreMatchers.is("456"));
    Assert.assertThat(target.getUser().getId(), CoreMatchers.is("123"));
    Assert.assertThat(target.getUser().getSecretCode(), CoreMatchers.is("789"));
  }
}
