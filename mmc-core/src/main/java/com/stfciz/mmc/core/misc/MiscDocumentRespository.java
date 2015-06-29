package com.stfciz.mmc.core.misc;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.stfciz.mmc.core.misc.domain.MiscDocument;

/**
 * 
 * @author stfciz
 *
 * 29 juin 2015
 */
public interface MiscDocumentRespository extends ElasticsearchRepository<MiscDocument, String> {

}
