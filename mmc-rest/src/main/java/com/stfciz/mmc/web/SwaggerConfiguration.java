package com.stfciz.mmc.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

@Configuration
@EnableSwagger
public class SwaggerConfiguration implements EnvironmentAware {
  
  private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);
  
  private RelaxedPropertyResolver propertyResolver;

  @Override
  public void setEnvironment(Environment environment) {
   this.propertyResolver = new RelaxedPropertyResolver(environment, "swagger.");
  }
  
  /**
   * Swagger Spring MVC configuration.
   */
  @Bean
  public SwaggerSpringMvcPlugin swaggerSpringMvcPlugin(SpringSwaggerConfig springSwaggerConfig) {
      log.debug("Starting Swagger");
    
      return new SwaggerSpringMvcPlugin(springSwaggerConfig)
          .apiInfo(apiInfo())
          .genericModelSubstitutes(ResponseEntity.class)
          .includePatterns("/music/md/.*","/photosets.*")
          .build();
  }

  /**
   * API Info as it appears on the swagger-ui page.
   */
  private ApiInfo apiInfo() {
      return new ApiInfo(
              propertyResolver.getProperty("title"),
              propertyResolver.getProperty("description"),
              propertyResolver.getProperty("termsOfServiceUrl"),
              propertyResolver.getProperty("contact"),
              propertyResolver.getProperty("license"),
              propertyResolver.getProperty("licenseUrl"));
  }

}
