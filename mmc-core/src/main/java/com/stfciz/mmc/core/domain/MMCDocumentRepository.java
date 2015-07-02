package com.stfciz.mmc.core.domain;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 
 * @author stfciz
 *
 */
public interface MMCDocumentRepository extends ElasticsearchRepository<MMCDocument, String> {

}