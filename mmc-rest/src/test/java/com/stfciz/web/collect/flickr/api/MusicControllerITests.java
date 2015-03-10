package com.stfciz.web.collect.flickr.api;

/**
 * 
 * @author ByTel
 *
 */
public class MusicControllerITests extends AbstractTests {

  @Override
  public String getAccessToken() throws Exception {
    return newAccessToken("localhost", 11000);
  }
  
//  @Test
//  public void getDocs() throws Exception {
//    String search = "endless";
//    final URI uri = URI.create(String.format("http://localhost:%d/music/md?q=%s&client_id=%s", this.port, search, "test"));
//    ResponseEntity<MusicDocumentsOut> response =  this.restTemplate.getForEntity(uri, MusicDocumentsOut.class);
//    Assert.assertThat(response.getBody().getDocs().size() > 0 , CoreMatchers.is(true));
//  }
}