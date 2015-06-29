package com.stfciz.mmc.core.book;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.stfciz.mmc.core.book.domain.BookDocument;

/**
 * 
 * @author stfciz
 *
 */
public interface BookDocumentRepository extends ElasticsearchRepository<BookDocument, String> {

}