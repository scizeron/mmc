package com.stfciz.mmc.web.service;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import com.stfciz.mmc.core.domain.DocumentType;
import com.stfciz.mmc.core.domain.MMCDocument;
import com.stfciz.mmc.web.api.ApiConverter;
import com.stfciz.mmc.web.api.FindResponse;

/**
 * 
 * @author stfciz
 *
 * 2 juil. 2015
 */
@Service
public class MMCServiceImpl implements MMCService, AggregationService {
 
  private static final String ALL_FIELD = "_all";
  
  private static final Sort MUSIC_ALL_SEARCH_SORT = new Sort(new Sort.Order(Sort.Direction.ASC, "artist")
  , new Sort.Order(Sort.Direction.ASC, "title"));

  private static final Sort DEFAULT_ALL_SEARCH_SORT = new Sort(new Sort.Order(Sort.Direction.ASC, "title")
  , new Sort.Order(Sort.Direction.ASC, "modified"));
  
  private static final Sort DEFAULT_SEARCH_SORT = new Sort(new Sort.Order(Sort.Direction.DESC, "modified")
    , new Sort.Order(Sort.Direction.ASC, "title"));
  
  
  @Autowired
  private ElasticsearchOperations elasticsearchOperations;
  
  @Autowired 
  @Qualifier("musicApiConverter")
  private ApiConverter<?,?> apiConverter;

  
  @Autowired
  private ElasticsearchRepository<MMCDocument, String> repository;
  
  @Override
  public MMCDocument findById(String id, DocumentType type) {
    MMCDocument doc = this.repository.findOne(id);
    if (type == null) {
      return doc;
    }
    return type.name().equals(doc.getType()) ? doc : null;
  }

  /**
   * 
   * @param type
   * @param allPages
   * @return
   */
  private Sort getSort(DocumentType type, boolean allPages) {
    return (type == null) 
          ? DEFAULT_SEARCH_SORT 
          : (allPages) 
            ? (DocumentType.MUSIC.equals(type)) 
              ? MUSIC_ALL_SEARCH_SORT 
              : DEFAULT_ALL_SEARCH_SORT 
            : DEFAULT_SEARCH_SORT;
  }

  @Override
  public FindResponse search(String query, int page, int pageSize, boolean allPages, DocumentType type) {
    Criteria criteria = null;
    
    if (type != null) {
      criteria = Criteria.where("type").is(type.name());
    }
    
    if (StringUtils.isNotBlank(query)) {
      if (criteria == null) {
        criteria = Criteria.where(ALL_FIELD).is(query);
      } else {
        criteria.and(Criteria.where(ALL_FIELD).is(query));
      }
    }
    
    if (criteria == null) {
      criteria = new Criteria();
    }
    
    Page<MMCDocument> result = this.elasticsearchOperations.queryForPage(new CriteriaQuery(criteria, new PageRequest(page, pageSize, getSort(type,allPages))), MMCDocument.class);
    
    FindResponse response = new FindResponse();
    
    response.setPrevious(result.hasPrevious());
    response.setPage(result.getNumber());
    response.setTotalPages(result.getTotalPages());
    response.setNext(result.hasNext());
    response.setPageSize(pageSize + response.getPageSize());
    
    if (result.hasContent()) {
      for (MMCDocument doc : result.getContent()) {
        response.getItems().add(this.apiConverter.convertToFindItemResponse(doc));
      }
    }

    return response;
  }

  @Override
  public Long getSumOfPurchases(DocumentType type) {
    final String agg = "getSumOfUpatedPrices";
    
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
      .withSearchType(org.elasticsearch.action.search.SearchType.COUNT)
      .addAggregation(AggregationBuilders.sum(agg).field("purchase.price"));
    
    withDocumentTypeQuery(queryBuilder, type);
    
    Aggregations aggregations = this.elasticsearchOperations.query(queryBuilder.build()
    , new ResultsExtractor<Aggregations>() {
        @Override
        public Aggregations extract(SearchResponse response) {
          return response.getAggregations();
        }
      });
    
    return Math.round(((Sum) aggregations.asMap().get(agg)).getValue());
  }

  /**
   * 
   * @param queryBuilder
   * @param type
   */
  private void withDocumentTypeQuery(NativeSearchQueryBuilder queryBuilder, DocumentType type) {
    if (type != null) {
      queryBuilder.withQuery(QueryBuilders.matchQuery("type", type.name()));
    }    
  }

  @Override
  public Long getSumOfUpatedPrices(DocumentType type) {
    final String agg = "getSumOfUpatedPrices";
    
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
      .withSearchType(org.elasticsearch.action.search.SearchType.COUNT)
      .addAggregation(AggregationBuilders.sum(agg).field("mostUpdatedPrice"));
    
    withDocumentTypeQuery(queryBuilder, type);
    
    Aggregations aggregations = this.elasticsearchOperations.query(queryBuilder.build()
    , new ResultsExtractor<Aggregations>() {
        @Override
        public Aggregations extract(SearchResponse response) {
          return response.getAggregations();
        }
      });
    
    return Math.round(((Sum) aggregations.asMap().get(agg)).getValue());
  }

  @Override
  public Long getMaxPurchase(DocumentType type) {
    final String bucketName = "purchase";
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
      .withSearchType(org.elasticsearch.action.search.SearchType.COUNT)
      .addAggregation(AggregationBuilders.max(bucketName).field("purchase.price"));
    
    withDocumentTypeQuery(queryBuilder, type);
    
    Aggregations aggregations = this.elasticsearchOperations.query(queryBuilder.build()
      , new ResultsExtractor<Aggregations>() {
          @Override
          public Aggregations extract(SearchResponse response) {
            return response.getAggregations();
          }
        });
    
    return Math.round(((InternalMax) aggregations.asMap().get(bucketName)).getValue());
  }
}
