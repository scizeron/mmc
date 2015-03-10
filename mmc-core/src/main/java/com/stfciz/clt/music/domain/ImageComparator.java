package com.stfciz.clt.music.domain;

import java.util.Comparator;

/**
 * 
 * @author stfciz
 *
 */
public class ImageComparator implements Comparator<Image> {
  @Override
  public int compare(Image img1, Image img2) {
    return img1.getOrder().compareTo(img2.getOrder());
  }

  private static ImageComparator INSTANCE = new ImageComparator();

  /**
   * 
   */
  private ImageComparator() {
  }

  public static ImageComparator get() {
    return INSTANCE;
  }
}
