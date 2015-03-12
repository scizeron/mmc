package com.stfciz.mmc.web;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@ComponentScan(basePackages={"com.stfciz.mmc"}, excludeFilters=@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=AppSpringWebConfiguration.class))
@EnableElasticsearchRepositories(basePackages={"com.stfciz.mmc"}, elasticsearchTemplateRef="elasticsearchOperations")
@EnableAutoConfiguration(exclude = { 
  ElasticsearchAutoConfiguration.class
, ElasticsearchDataAutoConfiguration.class
})
public class AppTestSpringWebConfiguration {

  /**
   * 
   * @return
   */
  @Bean
  public Client elasticSearchClient() {
    ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder()
        .put("http.enabled", String.valueOf(false))
        .put("index.store.type", "memory")
        .put("node.local", String.valueOf(true))
        .put("path.logs", "target/es/log")
        .put("path.data", "target/es/data")
        .put("path.work", "target/es/work")
        .put("path.config", "target/es/config")
        ;
    
    Node node = new NodeBuilder().settings(settings)
        .clusterName("test").node();
    
    return node.client();
  }
  
  @Bean
  public ElasticsearchOperations elasticsearchOperations(Client client) {
    return new ElasticsearchTemplate(client);
  }
}
