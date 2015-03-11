package com.stfciz.mmc.web.controller.music;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.elasticsearch.client.Client;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tlrx.elasticsearch.test.EsSetup;
import com.stfciz.mmc.web.AbstractWebApplicationTests;
import com.stfciz.mmc.web.api.music.FindResponse;
/**
 * 
 * @author ByTel
 *
 */
public class MusicControllerTests extends AbstractWebApplicationTests {

  private ObjectMapper mapper = new ObjectMapper();
  
  private EsSetup esSetup;
 
  @Autowired
  private Client client;
  
  @Before
  public void setUpIndex() {
    this.esSetup = new EsSetup(this.client);
    this.esSetup.execute(EsSetup.deleteAll(), EsSetup.createIndex("music").withData(EsSetup.fromClassPath("music.json")));
  }
  
  /**
   * 
   * @throws Exception
   */
  @Test public void findLastModifiedDocuments() throws Exception {
    // when
    MvcResult result = this.mockMvc.perform(get("/music/md"))
      .andDo(print())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful()).andReturn();
    // then
    FindResponse response = this.mapper.readValue(result.getResponse().getContentAsString(), FindResponse.class);
    Assert.assertThat(response.getDocs().size(), CoreMatchers.is(3));
    Assert.assertThat(response.getDocs().get(0).getModified().after(response.getDocs().get(1).getModified()), CoreMatchers.is(true));
    Assert.assertThat(response.getDocs().get(1).getModified().after(response.getDocs().get(2).getModified()), CoreMatchers.is(true));
  }
  
//  /**
//   * 
//   * @throws Exception
//   */
//  @Test public void findByCriteria() throws Exception {
//    // when
//    MvcResult result = this.mockMvc.perform(get("/music/md").param("q", "pink floyd"))
//      .andDo(print())
//      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//      .andExpect(status().is2xxSuccessful()).andReturn();
//    // then
//    FindResponse response = this.mapper.readValue(result.getResponse().getContentAsString(), FindResponse.class);
//    Assert.assertThat(response.getDocs().size(), CoreMatchers.is(3));
//  }
}