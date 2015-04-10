package com.stfciz.mmc.web.api.music;

import java.util.UUID;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.stfciz.mmc.core.music.domain.MusicDocument;
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
    MusicDocument result = this.apiConverter.convertNewMusicRequestIn(request);
    // then
    Assert.assertThat("The id is generated", result.getId(), CoreMatchers.notNullValue());
  }
  
  /**
   * 
   */
  @Test public void convertMusicDocumentToFindDocument() {
    // given
    MusicDocument doc = newMusicDocument();
    // when
    AbstractBaseResponse result = this.apiConverter.convertMusicDocumentToFindDocument(doc);
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
    MusicDocument result = this.apiConverter.convertUpdateMusicRequestIn(request);
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
    AbstractBaseResponse result = this.apiConverter.convertMusicDocumentToFindDocument(doc);
    // then
    Assert.assertThat(result.getId(), CoreMatchers.notNullValue());
  }
}
