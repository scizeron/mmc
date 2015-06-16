package com.stfciz.mmc.web.api.music;

import java.util.UUID;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.stfciz.mmc.core.music.domain.MusicDocument;
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
  private MusicDocument newMusicDocument() {
    MusicDocument doc = new MusicDocument();
    doc.setId(UUID.randomUUID().toString());
    return doc;
  }

  /**
   * 
   */
  @Test public void convertNewMusicRequestIn() {
    // given
    NewRequest request = new NewRequest();
    // when
    MusicDocument result = this.apiConverter.convertNewRequestContent(request);
    // then
    Assert.assertThat("The id is generated", result.getId(), CoreMatchers.notNullValue());
  }
  
  /**
   * 
   */
  @Test public void convertNewMusicRequestInWithPurchasePrice() {
    // given
    NewRequest request = new NewRequest();
    request.setPurchasePrice(153);
    
    // when
    MusicDocument result = this.apiConverter.convertNewRequestContent(request);
    // then
    Assert.assertThat("The id is generated", result.getId(), CoreMatchers.notNullValue());
    Assert.assertThat(result.getMostUpdatedPrice(), CoreMatchers.is(new Integer(153)));
  }
  
  /**
   * 
   */
  @Test public void convertMusicDocumentToFindDocument() {
    // given
    MusicDocument doc = newMusicDocument();
    // when
    AbstractMusicBaseResponse result = this.apiConverter.convertToFindDocument(doc);
    // then
    Assert.assertThat(result.getId(), CoreMatchers.notNullValue());
  }
  
  /**
   * 
   */
  @Test public void convertUpdateMusicRequestIn() {
    // given
    UpdateRequest request = new UpdateRequest();
    request.setId("123456");
    // when
    MusicDocument result = this.apiConverter.convertUpdateRequestContent(request);
    // then
    Assert.assertThat("The id is copied", result.getId(), CoreMatchers.is("123456"));
  }
  
  /**
   * 
   */
  @Test public void convertMusicDocumentToGetResponse() {
    // given
    MusicDocument doc = newMusicDocument();
    // when
    AbstractMusicBaseResponse result = this.apiConverter.convertToFindDocument(doc);
    // then
    Assert.assertThat(result.getId(), CoreMatchers.notNullValue());
  }
  
  /**
   * 
   */
  @Test public void convertUpdateMusicRequestInWithUpdatedPrices() {
    // given
    UpdateRequest request = new UpdateRequest();
    request.setId("123456");
    request.setPurchasePrice(153);
    request.getPrices().add(new UpdatePrice(120,2007,12));
    request.getPrices().add(new UpdatePrice(80,2008,11));
    request.getPrices().add(new UpdatePrice(125,2008,12));
    request.getPrices().add(new UpdatePrice(75,2003,12));
    // when
    MusicDocument result = this.apiConverter.convertUpdateRequestContent(request);
    // then
    Assert.assertThat(result.getMostUpdatedPrice(), CoreMatchers.is(new Integer(125)));
  }
}
