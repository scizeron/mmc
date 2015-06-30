package com.stfciz.mmc.web.service.impl;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import com.stfciz.mmc.web.service.AggregationService;

@Component
public class AggregationServiceImpl implements AggregationService {

  @Autowired
  private ElasticsearchOperations elasticsearchOperations;
  
  @Override
  public Long getSumOfPurchases(String [] indices, String [] types) {
    final String agg = "getSumOfUpatedPrices";
    
    Aggregations aggregations = this.elasticsearchOperations.query(
      new NativeSearchQueryBuilder()
        .withQuery(matchAllQuery())
        .withSearchType(org.elasticsearch.action.search.SearchType.COUNT)
        .withIndices(indices)
        .withTypes(types)
        .addAggregation(AggregationBuilders.sum(agg).field("purchase.price")).build()
    , new ResultsExtractor<Aggregations>() {
        @Override
        public Aggregations extract(SearchResponse response) {
          return response.getAggregations();
        }
      });
    
    return Math.round(((Sum) aggregations.asMap().get(agg)).getValue());
  }

  @Override
  public Long getSumOfUpatedPrices(String [] indices, String [] types) {
    final String agg = "getSumOfUpatedPrices";
    
    Aggregations aggregations = this.elasticsearchOperations.query(
      new NativeSearchQueryBuilder()
        .withQuery(matchAllQuery())
        .withSearchType(org.elasticsearch.action.search.SearchType.COUNT)
        .withIndices(indices)
        .withTypes(types)
        .addAggregation(AggregationBuilders.sum(agg).field("mostUpdatedPrice")).build()
    , new ResultsExtractor<Aggregations>() {
        @Override
        public Aggregations extract(SearchResponse response) {
          return response.getAggregations();
        }
      });
    
    return Math.round(((Sum) aggregations.asMap().get(agg)).getValue());
  }

  @Override
  public Long getMaxPurchase(String [] indices, String [] types) {
    final String bucketName = "purchase";
    Aggregations aggregations = this.elasticsearchOperations.query(
        new NativeSearchQueryBuilder()
          .withQuery(matchAllQuery())
          .withSearchType(org.elasticsearch.action.search.SearchType.COUNT)
          .withIndices(indices)
          .withTypes(types)
          .addAggregation(AggregationBuilders.max(bucketName).field("purchase.price")).build()
      , new ResultsExtractor<Aggregations>() {
          @Override
          public Aggregations extract(SearchResponse response) {
            return response.getAggregations();
          }
        });
    
    return Math.round(((InternalMax) aggregations.asMap().get(bucketName)).getValue());
  }
}
