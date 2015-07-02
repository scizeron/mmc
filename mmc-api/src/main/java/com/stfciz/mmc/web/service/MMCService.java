package com.stfciz.mmc.web.service;



import com.stfciz.mmc.core.domain.DocumentType;
import com.stfciz.mmc.core.domain.MMCDocument;
import com.stfciz.mmc.web.api.FindResponse;

/**
 * 
 * @author stfciz
 *
 * 2 juil. 2015
 */
public interface MMCService {

  /**
   * 
   * @param id
   * @param type
   * @return
   */
  MMCDocument findById(String id, DocumentType type);

  /**
   * 
   * @param query
   * @param page
   * @param pageSize
   * @param allPages
   * @param type
   * @return
   */
  FindResponse search(String query, int page, int pageSize, boolean allPages, DocumentType type);
  
}
