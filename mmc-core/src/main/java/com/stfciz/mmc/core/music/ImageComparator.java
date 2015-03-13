package com.stfciz.mmc.core.music;

import java.util.Comparator;

import com.stfciz.mmc.core.music.domain.PhotoMusicDocument;

/**
 * 
 * @author stfciz
 *
 */
public class ImageComparator implements Comparator<PhotoMusicDocument> {
  
  private static ImageComparator INSTANCE = new ImageComparator();
  
  /**
   * 
   */
  private ImageComparator() {
  }

  public static ImageComparator get() {
    return INSTANCE;
  }
  
  @Override
  public int compare(PhotoMusicDocument img1, PhotoMusicDocument img2) {
    if (img1.getOrder() == null && img2.getOrder() == null) {
      return 0;
    }
    
    if (img1.getOrder() == null && img2.getOrder() != null) {
      return Integer.MAX_VALUE;
    }
    
    if (img1.getOrder() != null && img2.getOrder() == null) {
      return Integer.MIN_VALUE;
    }
    
    return img1.getOrder().compareTo(img2.getOrder());
  }
}
