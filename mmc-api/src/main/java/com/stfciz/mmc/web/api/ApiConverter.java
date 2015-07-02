package com.stfciz.mmc.web.api;

import com.stfciz.mmc.core.domain.DocumentType;
import com.stfciz.mmc.core.domain.MMCDocument;

/**
 * 
 * @author stfciz
 *
 * 2 juil. 2015
 */
public interface ApiConverter<GR extends FindItemResponse, SR extends AbstractSaveRequest> {

  /**
   * 
   * @return
   */
  public abstract GR newGetResponse();

  /**
   * 
   * @return
   */
  public abstract DocumentType getDocumentType();

  /**
   * 
   * @param src
   * @return
   */
  public abstract FindItemResponse convertToFindItemResponse(MMCDocument src);

  /**
   * 
   * @param doc
   * @return
   */
  public abstract GR convertToGetResponse(MMCDocument doc);

  /**
   * 
   * @param request
   * @return
   */
  public abstract MMCDocument convertFromNewRequest(SR request);

  /**
   * 
   * @param request
   * @return
   */
  public abstract MMCDocument convertFromUpdateRequest(SR request);

}