package com.stfciz.mmc.web;

import org.elasticsearch.client.Client;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.tlrx.elasticsearch.test.EsSetup;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = { AppTestSpringWebConfiguration.class})
@SpringApplicationConfiguration(classes = { AppSpringWebConfiguration.class})
@WebAppConfiguration
@ActiveProfiles("test")
public class AbstractWebApplicationTests {

  protected MockMvc mockMvc;
  
  protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractWebApplicationTests.class); 
  
  @Autowired
  private WebApplicationContext context;
  
  private EsSetup esSetup;
  
  @Autowired
  private Client client;
  
  @BeforeClass
  public static void initEnv() {
    System.setProperty("spring.config.location","src/test/resources/configuration.yml");
    System.setProperty("external.config.dir", "src/test/resources");
  }
  
  @Before
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    this.esSetup = new EsSetup(this.client);
    this.esSetup.execute(
          EsSetup.deleteAll()
        , EsSetup.createIndex("music").withData(EsSetup.fromClassPath("indices/dataset.json"))
        );
  }
  
}
