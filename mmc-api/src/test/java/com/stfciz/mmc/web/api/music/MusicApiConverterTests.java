package com.stfciz.mmc.web.api.music;

import java.util.UUID;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.stfciz.mmc.core.domain.MMCDocument;
import com.stfciz.mmc.web.api.FindItemResponse;
import com.stfciz.mmc.web.api.UpdatePrice;
import com.stfciz.mmc.web.api.photo.PhotoApiConverter;
/**
 * 
 * @author stfciz
 *
 */
public class MusicApiConverterTests {
  
  private MusicApiConverter apiConverter = new MusicApiConverter(new PhotoApiConverter());
  
  /**
   * 
   * @return
   */
  private MMCDocument newMusicDocument() {
    MMCDocument doc = new MMCDocument();
    doc.setId(UUID.randomUUID().toString());
    return doc;
  }

  /**
   * 
   */
  @Test public void convertFromNewRequest() {
    // given
    SaveRequest request = new SaveRequest();
    // when
    MMCDocument result = this.apiConverter.convertFromNewRequest(request);
    // then
    Assert.assertThat("The id is generated", result.getId(), CoreMatchers.notNullValue());
  }
  
  /**
   * 
   */
  @Test public void convertFromNewRequestWithPurchasePrice() {
    // given
    SaveRequest request = new SaveRequest();
    request.setPurchasePrice(153);
    
    // when
    MMCDocument result = this.apiConverter.convertFromNewRequest(request);
    // then
    Assert.assertThat("The id is generated", result.getId(), CoreMatchers.notNullValue());
    Assert.assertThat(result.getMostUpdatedPrice(), CoreMatchers.is(new Integer(153)));
  }
  
  /**
   * 
   */
  @Test public void convertMusicDocumentToFindDocument() {
    // given
    MMCDocument doc = newMusicDocument();
    // when
    FindItemResponse result = this.apiConverter.convertToFindItemResponse(doc);
    // then
    Assert.assertThat(result.getId(), CoreMatchers.notNullValue());
  }
  
  /**
   * 
   */
  @Test public void convertFromUpdateRequest() {
    // given
    SaveRequest request = new SaveRequest();
    request.setId("123456");
    // when
    MMCDocument result = this.apiConverter.convertFromUpdateRequest(request);
    // then
    Assert.assertThat("The id is copied", result.getId(), CoreMatchers.is("123456"));
  }
  
  /**
   * 
   */
  @Test public void convertMusicDocumentToGetResponse() {
    // given
    MMCDocument doc = newMusicDocument();
    // when
    FindItemResponse result = this.apiConverter.convertToFindItemResponse(doc);
    // then
    Assert.assertThat(result.getId(), CoreMatchers.notNullValue());
  }
  
  /**
   * 
   */
  @Test public void convertUpdateMusicRequestInWithUpdatedPrices() {
    // given
    SaveRequest request = new SaveRequest();
    request.setId("123456");
    request.setPurchasePrice(153);
    request.getPrices().add(new UpdatePrice(120,2007,12));
    request.getPrices().add(new UpdatePrice(80,2008,11));
    request.getPrices().add(new UpdatePrice(125,2008,12));
    request.getPrices().add(new UpdatePrice(75,2003,12));
    // when
    MMCDocument result = this.apiConverter.convertFromUpdateRequest(request);
    // then
    Assert.assertThat(result.getMostUpdatedPrice(), CoreMatchers.is(new Integer(125)));
  }
}
