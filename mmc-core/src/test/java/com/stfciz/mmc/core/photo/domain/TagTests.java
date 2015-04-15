package com.stfciz.mmc.core.photo.domain;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Bellevue
 *
 */
public class TagTests {

  /**
   * 
   */
  @Test public void toFlickrFormatAndRevert() {
    final String docId = "fce683be-2062-47e7-8e2e-15d42b17b554";
    
    final Tag tag = new Tag(TagName.DOC_ID, docId);
    final String flickrFormat = tag.toFlickrFormat();
    Assert.assertThat(flickrFormat, CoreMatchers.is("docidc102c99c101c54c56c51c98c101c45c50c48c54c50c45c52c55c101c55c45c56c101c50c101c45c49c53c100c52c50c98c49c55c98c53c53c52"));
    
    final Tag newTag = Tag.fromFlickrFormat(flickrFormat);
    Assert.assertThat(newTag.getName(), CoreMatchers.is(TagName.DOC_ID));
    Assert.assertThat(newTag.getValue(), CoreMatchers.is(docId));
  }
}
