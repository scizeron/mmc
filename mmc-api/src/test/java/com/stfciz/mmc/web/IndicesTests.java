package com.stfciz.mmc.web;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

/**
 * 
 * @author stfciz
 *
 * 30 juin 2015
 */
public class IndicesTests extends AbstractWebApplicationTests {
  
  @Autowired
  private ElasticsearchOperations elasticsearchOperations;
  
  @Test public void checkMusicIndice() {
    checkIndice("music", "md", 10);
  }

  /**
   * 
   * @param indice
   * @param type
   * @param expetedCount
   */
  private void checkIndice(String indice, String type, long expetedCount) {
    SearchQuery searchQuery = new NativeSearchQueryBuilder()
      .withQuery(matchAllQuery())
      .withIndices(indice)
      .withTypes(type).build();
      
    Assert.assertThat(this.elasticsearchOperations.count(searchQuery), CoreMatchers.is(expetedCount));
  }
}
