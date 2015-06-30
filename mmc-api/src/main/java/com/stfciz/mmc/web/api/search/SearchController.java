package com.stfciz.mmc.web.api.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.base.Strings;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.FacetedPageImpl;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.data.elasticsearch.core.facet.FacetResult;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stfciz.mmc.web.api.AbstractApiConverter;
import com.stfciz.mmc.web.api.AbstractBaseResponse;
import com.stfciz.mmc.web.api.book.BookApiConverter;
import com.stfciz.mmc.web.api.misc.MiscApiConverter;
import com.stfciz.mmc.web.api.music.MusicApiConverter;
import com.stfciz.mmc.web.oauth2.OAuth2ScopeApi;
import com.stfciz.mmc.web.oauth2.Permission;
import com.stfciz.mmc.web.service.FindRequestHandler;

@RestController
@RequestMapping(value = "/search", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SearchController {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);
  
  @Autowired
  private ElasticsearchOperations elasticsearchOperations;
  
  @Autowired
  private FindRequestHandler findRequestHandler;
  
  private ResultsMapper resultsMapper;
    
  @Autowired
  private BookApiConverter bookApiConverter;
  
  @Autowired
  private MusicApiConverter musicApiConverter;
  
  @Autowired
  private MiscApiConverter miscApiConverter;
  
  private String [] indices;
  
  private String [] types;
  
  @SuppressWarnings("rawtypes")
  private Map<Class<?>, AbstractApiConverter> converters;
  
  @SuppressWarnings("rawtypes")
  @PostConstruct
  public void postConstruct() throws Exception {
    this.converters = new HashMap<Class<?>, AbstractApiConverter>();
    Map<String, Class<?>> typeHandlers = new HashMap<String, Class<?>>();
    List<String> indices = new ArrayList<String>();
    List<String> types = new ArrayList<String>();
    // com.stfciz.mmc.core
    Reflections  reflections = new Reflections("com.stfciz.mmc.core");
    Set<Class<?>> clazzDocuments = reflections.getTypesAnnotatedWith(Document.class);
    
    for (Class<?> clazzDocument : clazzDocuments) {
      Document document = clazzDocument.getAnnotation(Document.class);
      indices.add(document.indexName());
      types.add(document.type());
      this.converters.put(clazzDocument, (AbstractApiConverter) this.getClass().getDeclaredField(document.indexName() + "ApiConverter").get(this));
      typeHandlers.put(document.indexName(),clazzDocument);
    }
    
    this.indices = indices.toArray(new String[indices.size()]);
    this.types = types.toArray(new String[types.size()]);
    this.resultsMapper = new MultiIndicesResultsMapper(typeHandlers);
  }
  
  @RequestMapping(method = RequestMethod.GET
      , consumes = { MediaType.ALL_VALUE })
  @Permission(scopes = { OAuth2ScopeApi.READ })
  @SuppressWarnings({ "rawtypes", "unused", "unchecked" })
  public com.stfciz.mmc.web.api.search.SearchResponse find(
      @RequestParam(value = "q", required = false) String query,
      @RequestParam(value = "p", required = false, defaultValue = "0") int page,
      @RequestParam(value = "s", required = false, defaultValue = "50") int pageSize) {

    PageRequest pageable = new PageRequest(page, pageSize, this.findRequestHandler.getSort(null));
    QueryBuilder queryBuilder = org.elasticsearch.index.query.QueryBuilders.matchAllQuery();
    
    if (org.apache.commons.lang3.StringUtils.isNotBlank(query)) {
      QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(query);
      this.findRequestHandler.customizeQueryStringQueryBuilder(queryStringQueryBuilder);
      queryBuilder = queryStringQueryBuilder;
    }
    
    SearchQuery searchQuery = new NativeSearchQueryBuilder()
              .withQuery(queryBuilder)
              .withPageable(pageable)
              .withIndices(this.indices)
              .withTypes(this.types)
              .build();
    
    FacetedPage<com.stfciz.mmc.core.domain.AbstractDocument> result = this.elasticsearchOperations.queryForPage(searchQuery, com.stfciz.mmc.core.domain.AbstractDocument.class, this.resultsMapper);
    
    com.stfciz.mmc.web.api.search.SearchResponse response = new com.stfciz.mmc.web.api.search.SearchResponse();
    response.setPageSize(result.getSize());
    response.setPage(result.getNumber());
    response.setTotalPages(result.getTotalPages());
    response.setNext(result.hasNext());
       
    if (result.hasContent()) {
      for (com.stfciz.mmc.core.domain.AbstractDocument doc : result.getContent()) {
        AbstractApiConverter converter = this.converters.get(doc.getClass());
        AbstractBaseResponse convertToFindElementResponse = converter.convertToFindElementResponse(doc);
        response.getItems().add(new SearchElementResponse(converter.convertToFindElementResponse(doc), StringUtils.replace(converter.getClass().getSimpleName(), "ApiConverter", "").toLowerCase()));
      }
    }
    
    LOGGER.debug("search \'{}\' [page:{}, pageSize:{}] => {} item(s)", new Object[]{query, page, pageSize, response.getItems().size()});
    
    return response;
  }
  
  /**
   * 
   * @author stfciz
   *
   * 30 juin 2015
   */
  static class MultiIndicesResultsMapper extends DefaultResultMapper {

    private Map<String, Class<?>> typeHandlers;
    
    /**
     * 
     * @param indices
     */
    public MultiIndicesResultsMapper(Map<String, Class<?>> typeHandlers) {
      super();
      this.typeHandlers =  typeHandlers;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> FacetedPage<T> mapResults(SearchResponse response,  Class<T> clazz, Pageable pageable) {
      long totalHits = response.getHits().totalHits();
      List<T> results = new ArrayList<T>();
      for (SearchHit hit : response.getHits()) {
        if (hit != null  && !Strings.isNullOrEmpty(hit.sourceAsString())) {
          results.add((T) mapEntity(hit.sourceAsString(), this.typeHandlers.get(hit.getIndex())));
        }
      }
      return new FacetedPageImpl<T>(results, pageable, totalHits, new ArrayList<FacetResult>());
    }
  }
}
