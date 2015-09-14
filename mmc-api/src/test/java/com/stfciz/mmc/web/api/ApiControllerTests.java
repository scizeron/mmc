package com.stfciz.mmc.web.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stfciz.mmc.web.AbstractWebApplicationTests;

/**
 * 
 * @author ByTel
 *
 */
public class ApiControllerTests extends AbstractWebApplicationTests {

  private ObjectMapper mapper = new ObjectMapper();
  
  /**
   * 
   * @throws Exception
   */
  @Test public void findLastModifiedDocuments() throws Exception {
    // when
    MvcResult result = this.mockMvc.perform(get("/music").accept(MediaType.ALL_VALUE))
      .andDo(print())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful()).andReturn();
    // then
    FindResponse response = this.mapper.readValue(result.getResponse().getContentAsString(), FindResponse.class);
    Assert.assertThat(response.getItems().get(0).getLastModified().after(response.getItems().get(1).getLastModified()) || response.getItems().get(0).getLastModified().equals(response.getItems().get(1).getLastModified()) , CoreMatchers.is(true));
  }
 
  /**
   * 
   * @throws Exception
   */
  @Test public void findSomeMusic() throws Exception {
    // when
    MvcResult result = this.mockMvc.perform(get("/music").param("q", "wall").accept(MediaType.ALL_VALUE))
      .andDo(print())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful()).andReturn();
    // then
    FindResponse response = this.mapper.readValue(result.getResponse().getContentAsString(), FindResponse.class);
    Assert.assertThat(response.getItems().size() > 0, CoreMatchers.is(true));
    for (FindItemResponse doc : response.getItems()) {
      Assert.assertThat(doc.getTitle().toLowerCase().contains("wall"), CoreMatchers.is(true));
      LOGGER.debug(this.mapper.writeValueAsString(doc));
      Assert.assertThat(doc.getType(), CoreMatchers.is("music"));
    }
  }
  
  /**
   * 
   * @throws Exception
   */
  @Test public void getMusic() throws Exception {
    // when
    MvcResult result = this.mockMvc.perform(get("/music/1").accept(MediaType.ALL_VALUE))
      .andDo(print())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful()).andReturn();

    // then
    com.stfciz.mmc.web.api.music.GetResponse response = this.mapper.readValue(result.getResponse().getContentAsString(), com.stfciz.mmc.web.api.music.GetResponse.class);
    Assert.assertThat(response.getId(), CoreMatchers.is("1"));
  }
  
  /**
   * 
   * @throws Exception
   */
  @Test public void findOneMusicsPageWihNoCriteria() throws Exception {
    // when
    MvcResult result = this.mockMvc.perform(get("/music").accept(MediaType.ALL_VALUE))
      .andDo(print())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful()).andReturn();
    // then
    FindResponse response = this.mapper.readValue(result.getResponse().getContentAsString(), FindResponse.class);
    Assert.assertThat(response.getItems().size(), CoreMatchers.is(6));
    for (FindItemResponse doc : response.getItems()) {
      LOGGER.debug(this.mapper.writeValueAsString(doc));
      Assert.assertThat(doc.getType(), CoreMatchers.is("music"));
    }
  }
  
  /**
   * 
   * @throws Exception
   */
  @Test public void findSomeBooks() throws Exception {
    // when
    MvcResult result = this.mockMvc.perform(get("/book").param("q", "more").accept(MediaType.ALL_VALUE))
      .andDo(print())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful()).andReturn();
    // then
    FindResponse response = this.mapper.readValue(result.getResponse().getContentAsString(), FindResponse.class);
    Assert.assertThat(response.getItems().size() > 0, CoreMatchers.is(true));
    for (FindItemResponse doc : response.getItems()) {
      Assert.assertThat(doc.getTitle().contains("More"), CoreMatchers.is(true));
      Assert.assertThat(doc.getType(), CoreMatchers.is("book"));
    }
  }
  
  /**
   * 
   * @throws Exception
   */
  @Test public void findOneBooksPageWihNoCriteria() throws Exception {
    // when
    MvcResult result = this.mockMvc.perform(get("/book").accept(MediaType.ALL_VALUE))
      .andDo(print())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful()).andReturn();
    // then
    FindResponse response = this.mapper.readValue(result.getResponse().getContentAsString(), FindResponse.class);
    Assert.assertThat(response.getItems().size() > 0, CoreMatchers.is(true));
    for (FindItemResponse doc : response.getItems()) {
      Assert.assertThat(doc.getType(), CoreMatchers.is("book"));
    }
  }
  
  /**
   * 
   * @throws Exception
   */
  @Test public void findMisc() throws Exception {
    // when
    MvcResult result = this.mockMvc.perform(get("/misc").accept(MediaType.ALL_VALUE))
      .andDo(print())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful()).andReturn();
    // then
    FindResponse response = this.mapper.readValue(result.getResponse().getContentAsString(), FindResponse.class);
    for (FindItemResponse doc : response.getItems()) {
      Assert.assertThat(doc.getType(), CoreMatchers.is("misc"));
    }
  }
  
  

}