package com.stfciz.clt.music.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.stfciz.clt.music.domain.MusicDocument;
/**
 * 
 * @author ByTel
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
  @Query("{\"bool\" : {\"must\" : {\"query_string\" : {\"query\" : \"?\",\"fields\" : [ \"artist\", \"title\"]}}}}")
  //@Query("{\"bool\" : {\"should\" : [ {\"field\" : {\"artist\" : \"?\"}}, {\"field\" : {\"title\" : \"?\"}} ]}}")
  Page<MusicDocument> search(String value, Pageable pageable);

  
//  {
//    "bool" : {
//      "must" : {
//        "query_string" : {
//          "query" : "animals",
//          "fields" : [ "title" ]
//        }
//      },
//      "should" : {
//        "query_string" : {
//          "query" : "animals",
//          "fields" : [ "artist" ]
//        }
//      }
//    }
//  }
  /**
   * 
   * @param title
   * @param artist
   * @param pageable
   * @return
   */
  Page<MusicDocument> findByTitleOrArtistAllIgnoreCaseOrderByTitleAsc(String title, String artist, Pageable pageable);
  
  /**
   * 
   * @param value
   * @param pageable
   * @return
   */
  Page<MusicDocument> findByTitleIgnoreCase(String value, Pageable pageable);
  
  
}