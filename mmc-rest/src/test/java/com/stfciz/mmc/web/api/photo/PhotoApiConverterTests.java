package com.stfciz.mmc.web.api.photo;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.stfciz.mmc.core.music.domain.PhotoMusicDocument;
/**
 * 
 * @author stfciz
 *
 */
public class PhotoApiConverterTests {
  
  private PhotoApiConverter apiConverter = new PhotoApiConverter();
  
  /**
   * 
   */
  @Test public void getSortedImages() {
    // given
    PhotoMusicDocument img1 = new PhotoMusicDocument();
    img1.setOrder(1);

    PhotoMusicDocument img2 = new PhotoMusicDocument();
    img2.setOrder(2);
    
    PhotoMusicDocument img3 = new PhotoMusicDocument();
    img3.setOrder(3);
    
    List<PhotoMusicDocument> images = new ArrayList<>();
    images.add(img3);
    images.add(img1);
    images.add(img2);
    
    // when
    List<PhotoMusicDocument> sortedImages = this.apiConverter.getSortedImages(images);
    Assert.assertThat(sortedImages.get(0).getOrder(),CoreMatchers.is(1));
    Assert.assertThat(sortedImages.get(1).getOrder(),CoreMatchers.is(2));
    Assert.assertThat(sortedImages.get(2).getOrder(),CoreMatchers.is(3));
  }
}
