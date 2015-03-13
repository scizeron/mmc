package com.stfciz.mmc.web;

import java.util.Arrays;

import org.elasticsearch.client.Client;
import org.springframework.boot.actuate.autoconfigure.AuditAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.CrshAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.EndpointMBeanExportAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.TraceRepositoryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.TraceWebFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.stfciz.mmc.web.controller.CorsFilter;
import com.stfciz.mmc.web.oauth2.OAuth2Filter;
import com.stfciz.mmc.web.oauth2.PermissionAspect;

/**
 * 
 * @author stfciz
 *
 */
@Configuration
@ComponentScan(basePackages={"com.stfciz.mmc"})
@EnableAspectJAutoProxy(proxyTargetClass=true)
@EnableElasticsearchRepositories(basePackages={"com.stfciz.mmc"}, elasticsearchTemplateRef="elasticsearchOperations")
@ImportResource({"classpath:applicationContext-clt-core.xml"})
@EnableAutoConfiguration(exclude = { 
    AuditAutoConfiguration.class
  , CrshAutoConfiguration.class
  , MetricFilterAutoConfiguration.class
  , MetricRepositoryAutoConfiguration.class
  , TraceRepositoryAutoConfiguration.class
  , TraceWebFilterAutoConfiguration.class
  , EndpointMBeanExportAutoConfiguration.class // exports de endpoints
  , ElasticsearchAutoConfiguration.class
  , ElasticsearchDataAutoConfiguration.class
})
public class AppSpringWebConfiguration {
  
  private static final String [] FILTER_URL_PATTERNS = {"/*"};
 
  @Bean 
  public FilterRegistrationBean getCORSFilter(CorsFilter filter) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
    registrationBean.setUrlPatterns(Arrays.asList(FILTER_URL_PATTERNS));
    registrationBean.setName(filter.getClass().getSimpleName());
    registrationBean.setOrder(1);
    return registrationBean;
  }
  
  @Bean 
  public FilterRegistrationBean getOAuth2Filter(OAuth2Filter filter) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
    registrationBean.setUrlPatterns(Arrays.asList(FILTER_URL_PATTERNS));
    registrationBean.setName(filter.getClass().getSimpleName());
    registrationBean.setOrder(2);
    return registrationBean;
  }
  
  @Bean
  public ElasticsearchOperations elasticsearchOperations(Client client) {
    return new ElasticsearchTemplate(client);
  }
  
  @Bean 
  @Profile("!test")
  public PermissionAspect permissionAspect() {
    return new PermissionAspect();
  }
}