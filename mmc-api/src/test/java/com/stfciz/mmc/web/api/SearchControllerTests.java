package com.stfciz.mmc.web.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stfciz.mmc.web.AbstractWebApplicationTests;
import com.stfciz.mmc.web.api.search.SearchElementResponse;
import com.stfciz.mmc.web.api.search.SearchResponse;


/**
 * 
 * @author ByTel
 *
 */
public class SearchControllerTests extends AbstractWebApplicationTests {

  private ObjectMapper mapper = new ObjectMapper();
  
  /**
   * 
   * @throws Exception
   */
  @Test public void search() throws Exception {
    // when
    MvcResult result = this.mockMvc.perform(get("/search?q=wall").accept(MediaType.ALL_VALUE))
      .andDo(print())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful()).andReturn();
    
    // then
    SearchResponse response = this.mapper.readValue(result.getResponse().getContentAsString(), SearchResponse.class);
    List<SearchElementResponse> items = response.getItems();
    Assert.assertThat(items.size(), CoreMatchers.is(4));
    for (SearchElementResponse searchElementResponse : items) {
      Assert.assertThat(searchElementResponse.getType(), CoreMatchers.anyOf(CoreMatchers.is("music"), CoreMatchers.is("book"), CoreMatchers.is("misc")));
    }
  }
 
}