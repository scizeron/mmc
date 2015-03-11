package com.stfciz.mmc.web.api.music;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.stfciz.mmc.core.music.domain.Image;
import com.stfciz.mmc.core.music.domain.MusicDocument;
/**
 * 
 * @author stfciz
 *
 */
public class ApiConverterTests {
  
  private ApiConverter apiConverter = new ApiConverter();
  
  private MusicDocument newMusicDocument() {
    MusicDocument doc = new MusicDocument();
    doc.setId(UUID.randomUUID().toString());
    return doc;
  }

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
    FindElement result = this.apiConverter.convertMusicDocumentToFindDocument(doc);
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
    FindElement result = this.apiConverter.convertMusicDocumentToFindDocument(doc);
    // then
    Assert.assertThat(result.getId(), CoreMatchers.notNullValue());
  }
  
  /**
   * 
   */
  @Test public void getSortedImages() {
    // given
    Image img1 = new Image();
    img1.setOrder(1);

    Image img2 = new Image();
    img2.setOrder(2);
    
    Image img3 = new Image();
    img3.setOrder(3);
    
    List<Image> images = new ArrayList<>();
    images.add(img3);
    images.add(img1);
    images.add(img2);
    
    // when
    List<Image> sortedImages = this.apiConverter.getSortedImages(images);
    Assert.assertThat(sortedImages.get(0).getOrder(),CoreMatchers.is(1));
    Assert.assertThat(sortedImages.get(1).getOrder(),CoreMatchers.is(2));
    Assert.assertThat(sortedImages.get(2).getOrder(),CoreMatchers.is(3));
  }
}
