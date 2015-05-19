package com.stfciz.mmc.core.music;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.stfciz.mmc.core.photo.domain.PhotoDocument;

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
    PhotoDocument img1 = new PhotoDocument();
    img1.setOrder(1);

    PhotoDocument img2 = new PhotoDocument();
    img2.setOrder(2);

    // when
    int compare = ImageComparator.get().compare(img1, img2);
    
    // then
    Assert.assertThat(compare, CoreMatchers.is(-1));
  }
}
