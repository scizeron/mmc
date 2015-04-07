package com.stfciz.mmc.web;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { AppTestSpringWebConfiguration.class})
@WebAppConfiguration
public class AbstractWebApplicationTests {

  protected MockMvc mockMvc;
  
  protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractWebApplicationTests.class); 
  
  @Autowired
  private WebApplicationContext context;
  
  @BeforeClass
  public static void initEnv() {
    System.setProperty("spring.profiles.active","test");
    System.setProperty("spring.config.location","src/test/resources/configuration.yml");
    System.setProperty("external.config.dir", "src/test/resources");
  }
  
  @Before
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
  }
  
}
