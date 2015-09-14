package com.stfciz.mmc.web;

import java.io.File;
import java.util.Arrays;

import javax.servlet.Filter;

import org.apache.catalina.filters.RemoteIpFilter;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.stfciz.mmc.web.oauth2.OAuth2Filter;
import com.stfciz.mmc.web.oauth2.PermissionAspect;

/**
 * 
 * @author stfciz
 *
 */
@Configuration
@ComponentScan(basePackages = { "com.stfciz.mmc" })
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableElasticsearchRepositories(basePackages = { "com.stfciz.mmc" }, elasticsearchTemplateRef = "elasticsearchOperations")
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
    , DataSourceAutoConfiguration.class
    , HibernateJpaAutoConfiguration.class    
})
public class AppSpringWebConfiguration {

  private static final String[] FILTER_URL_PATTERNS = { "/*" };
  
  @Autowired
  private ApplicationContext applicationContext;
  
  /**
   * 
   * @param filter
   * @param order
   * @return
   */
  private FilterRegistrationBean newFilterRegistration(Filter filter , int order) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
    registrationBean.setUrlPatterns(Arrays.asList(FILTER_URL_PATTERNS));
    registrationBean.setName(filter.getClass().getSimpleName());
    registrationBean.setOrder(order);
    return registrationBean;
  }
  
  @Bean
  FilterRegistrationBean remoteIpFilter() {
    RemoteIpFilter filter = new RemoteIpFilter();
    filter.setProtocolHeader("X-Forwarded-Proto");
    return newFilterRegistration(filter, 1);
  }
  
  @Bean
  FilterRegistrationBean loggingFilter() {
    return newFilterRegistration(new LoggingFilter(), 2);
  }
  
  @Bean
  public FilterRegistrationBean getCORSFilter() {
    return newFilterRegistration(new CorsFilter(), 3);
  }

  @Bean
  public FilterRegistrationBean getOAuth2Filter() {
    return newFilterRegistration(new OAuth2Filter(), 4);
  }
  
  @Bean
  public Client elasticSearchClient(AppConfiguration appConfiguration) {
    if (Arrays.asList(applicationContext.getEnvironment().getActiveProfiles()).contains("test")) {
      ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder()
          .put("http.enabled", String.valueOf(false))
          .put("index.store.type", "memory")
          .put("node.local", String.valueOf(true))
          .put("path.logs", "target/es/log")
          .put("path.data", "target/es/data")
          .put("path.work", "target/es/work")
          .put("path.config", "target/es/config")
          ;
      
      return new NodeBuilder().settings(settings).clusterName("test").node().client();
    } 

    if (!new File(appConfiguration.getEsDirectory()).exists()) {
      new File(appConfiguration.getEsDirectory() + "/data").mkdirs();
      new File(appConfiguration.getEsDirectory() + "/work").mkdirs();
      new File(appConfiguration.getEsDirectory() + "/log").mkdirs();
    }
  
    ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder()
        .put("cluster.name", "elastic-mmc")
        .put("node.name", "rest-mmc")
        .put("node.master", true)
        .put("node.data", true)
        .put("index.number_of_shards", 1)
        .put("index.number_of_replicas", 0)
        .put("http.enabled", true)
        .put("path.data", appConfiguration.getEsDirectory() + "/data")
        .put("path.work", appConfiguration.getEsDirectory() + "/work")
        .put("path.logs", appConfiguration.getEsDirectory() + "/log")
        ;
    
    return new NodeBuilder().loadConfigSettings(false).settings(settings).node().client();
  }
  
  @Bean
  @Profile("!test")
  public PermissionAspect permissionAspect() {
    return new PermissionAspect();
  }

  @Bean
  public ElasticsearchOperations elasticsearchOperations(Client client) {
    return new ElasticsearchTemplate(client);
  }

  @Bean
  public HttpMessageConverters customConverters() {
    return new HttpMessageConverters(new com.stfciz.mmc.web.api.book.PdfHttpMessageFindResponseConverter()
                                   , new com.stfciz.mmc.web.api.music.PdfHttpMessageFindResponseConverter()                               
                                   , new com.stfciz.mmc.web.api.misc.PdfHttpMessageFindResponseConverter());
  }

}