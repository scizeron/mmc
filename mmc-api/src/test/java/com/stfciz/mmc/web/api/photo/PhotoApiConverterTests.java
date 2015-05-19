package com.stfciz.mmc.web.api.photo;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.stfciz.mmc.core.photo.domain.PhotoDocument;
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
    PhotoDocument img1 = new PhotoDocument();
    img1.setOrder(1);

    PhotoDocument img2 = new PhotoDocument();
    img2.setOrder(2);
    
    PhotoDocument img3 = new PhotoDocument();
    img3.setOrder(3);
    
    List<PhotoDocument> images = new ArrayList<>();
    images.add(img3);
    images.add(img1);
    images.add(img2);
    
    // when
    List<PhotoDocument> sortedImages = this.apiConverter.getSortedImages(images);
    Assert.assertThat(sortedImages.get(0).getOrder(),CoreMatchers.is(1));
    Assert.assertThat(sortedImages.get(1).getOrder(),CoreMatchers.is(2));
    Assert.assertThat(sortedImages.get(2).getOrder(),CoreMatchers.is(3));
  }
}
