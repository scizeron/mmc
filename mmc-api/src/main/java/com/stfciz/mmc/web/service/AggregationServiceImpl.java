package com.stfciz.mmc.web.service;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

@Component
public class AggregationServiceImpl implements AggregationService {

  @Autowired
  private ElasticsearchOperations elasticsearchOperations;
  
  @Override
  public Long getSumOfPurchases(String type) {
    final String bucketName = "purchase";
    
    Aggregations aggregations = this.elasticsearchOperations.query(
      new NativeSearchQueryBuilder()
        .withQuery(matchAllQuery())
        .withSearchType(org.elasticsearch.action.search.SearchType.COUNT)
        .withTypes(type)
        .addAggregation(terms(bucketName).field("price")).build()
    , new ResultsExtractor<Aggregations>() {
        @Override
        public Aggregations extract(SearchResponse response) {
          return response.getAggregations();
        }
      });
    
    return ((LongTerms) aggregations.asMap().get(bucketName)).getBuckets().stream().map(Terms.Bucket::getKeyAsNumber).mapToLong(Number::intValue).sum();
  }

  @Override
  public Long getSumOfUpatedPrices(String type) {
    
    /** 
     * sum (purchase.price when prices is missing + 
     * 
     * 
     */
    
    
   return null;
  }

  @Override
  public Long getMaxPurchase(String type) {
    final String bucketName = "purchase";
    Aggregations aggregations = this.elasticsearchOperations.query(
        new NativeSearchQueryBuilder()
          .withQuery(matchAllQuery())
          .withSearchType(org.elasticsearch.action.search.SearchType.COUNT)
          .withTypes(type)
          .addAggregation(AggregationBuilders.max(bucketName).field("price")).build()
      , new ResultsExtractor<Aggregations>() {
          @Override
          public Aggregations extract(SearchResponse response) {
            return response.getAggregations();
          }
        });
    
    return Math.round(((InternalMax) aggregations.asMap().get(bucketName)).getValue());
  }
}
