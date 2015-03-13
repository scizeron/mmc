package com.stfciz.mmc.core.music;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.stfciz.mmc.core.music.domain.PhotoMusicDocument;

/**
 * 
 * @author stfciz
 *
 */
public class ImageComparatorTests {
  
  /**
   * 
   */
  @Test public void compare() {
    // given
    PhotoMusicDocument img1 = new PhotoMusicDocument();
    img1.setOrder(1);

    PhotoMusicDocument img2 = new PhotoMusicDocument();
    img2.setOrder(2);

    // when
    int compare = ImageComparator.get().compare(img1, img2);
    
    // then
    Assert.assertThat(compare, CoreMatchers.is(-1));
  }
}
