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


/**
 * 
 * @author ByTel
 *
 */
public class SearchControllerTests extends AbstractWebApplicationTests {

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
    FindResponse response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), FindResponse.class);
    List<FindItemResponse> items = response.getItems();
    Assert.assertThat(items.size(), CoreMatchers.is(4));
    for (FindItemResponse item : items) {
      Assert.assertThat(item.getType(), CoreMatchers.anyOf(CoreMatchers.is("music"), CoreMatchers.is("book"), CoreMatchers.is("misc")));
    }
  }
 
}