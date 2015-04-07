package com.stfciz.mmc.web;

import java.util.Arrays;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.NodeBuilder;
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
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.stfciz.mmc.web.api.music.PdfHttpMessageFindResponseConverter;
import com.stfciz.mmc.web.controller.CorsFilter;
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
@EnableAutoConfiguration(exclude = { AuditAutoConfiguration.class
    , CrshAutoConfiguration.class
    , MetricFilterAutoConfiguration.class
    , MetricRepositoryAutoConfiguration.class
    , TraceRepositoryAutoConfiguration.class
    , TraceWebFilterAutoConfiguration.class
    , EndpointMBeanExportAutoConfiguration.class // exports de endpoints
    , ElasticsearchAutoConfiguration.class
    , ElasticsearchDataAutoConfiguration.class })
public class AppSpringWebConfiguration {

  private static final String[] FILTER_URL_PATTERNS = { "/*" };
  
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
  @Profile("!test")
  public Client elasticSearchClient(AppConfiguration appConfiguration) {
    ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder()
        .put("cluster.name", "elastic-mmc")
        .put("node.name", "rest-mmc")
        .put("node.master", String.valueOf(true))
        .put("node.data", String.valueOf(true))
        .put("index.number_of_shards", String.valueOf(1))
        .put("index.number_of_replicas", String.valueOf(0))
        .put("http.enabled", String.valueOf(false))
        .put("path.data", appConfiguration.getEsDirectory() + "/data")
        .put("path.work", appConfiguration.getEsDirectory() + "/work")
        .put("path.logs", appConfiguration.getEsDirectory() + "/log")
        ;
    
    return new NodeBuilder().loadConfigSettings(false).settings(settings).node().client();
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
  
  @Bean
  public HttpMessageConverters customConverters() {
    return new HttpMessageConverters(new PdfHttpMessageFindResponseConverter());
  }

}