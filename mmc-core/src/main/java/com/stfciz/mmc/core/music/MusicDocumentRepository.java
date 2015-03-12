package com.stfciz.mmc.core.music;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.stfciz.mmc.core.music.domain.MusicDocument;

/**
 * 
 * @author stfciz
 *
 */
public interface MusicDocumentRepository extends ElasticsearchRepository<MusicDocument, String> {
 
  /**
   * 
   * @param value
   * @param pageable
   * @return
   */
  //@Query("{\"bool\" : {\"must\" : {\"query_string\" : {\"query\" : \"*?*\",\"fields\" : [ \"title\", \"artist\" ],\"analyze_wildcard\" : true}}}}")
  //@Query("{\"bool\" : {\"must\" : {\"query_string\" : {\"query\" : \"?\",\"fields\" : [ \"artist\", \"title\"]}}}}")
  //@Query("{\"bool\" : {\"should\" : [ {\"field\" : {\"artist\" : \"?\"}}, {\"field\" : {\"title\" : \"?\"}} ]}}")
  @Query("{\"bool\" : {\"should\" : {\"query_string\" : {\"query\" : \"*?*\",\"fields\" : [ \"title\" ],\"analyze_wildcard\" : true }},\"should\" : {\"query_string\" : {\"query\" : \"*?*\",\"fields\" : [ \"artist\" ],\"analyze_wildcard\" : true}}}}")
  Page<MusicDocument> search(String title, Pageable pageable);
}