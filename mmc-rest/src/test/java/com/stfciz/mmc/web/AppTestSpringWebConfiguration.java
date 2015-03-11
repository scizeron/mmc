package com.stfciz.mmc.web;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@ComponentScan(basePackages={"com.stfciz.mmc"})
@EnableElasticsearchRepositories(basePackages={"com.stfciz.mmc"})
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class AppTestSpringWebConfiguration {

  /**
   * 
   * @return
   */
  @Bean
  @Primary
  public Client elasticSearchClient() {
    ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder()
        .put("http.enabled", String.valueOf(false))
        .put("index.store.type", "memory")
        .put("node.local", String.valueOf(true))
        .put("path.logs", "target/es/log")
        .put("path.data", "target/es/data")
        ;
    
    Node node = new NodeBuilder().settings(settings)
        .clusterName("test").node();
    
    return node.client();
  }
}
