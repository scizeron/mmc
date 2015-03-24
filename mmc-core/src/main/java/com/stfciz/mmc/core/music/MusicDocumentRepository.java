package com.stfciz.mmc.core.music;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.stfciz.mmc.core.music.domain.MusicDocument;

/**
 * 
 * @author stfciz
 *
 */
public interface MusicDocumentRepository extends ElasticsearchRepository<MusicDocument, String> {

}