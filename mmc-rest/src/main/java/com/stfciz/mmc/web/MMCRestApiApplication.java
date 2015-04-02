package com.stfciz.mmc.web;

import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 
 * @author stfciz
 *
 */
public class MMCRestApiApplication {

  /**
   * 
   * @param args
   */
  public static void main(String[] args) {
    final SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();
    springApplicationBuilder.sources(AppSpringWebConfiguration.class);
    springApplicationBuilder.showBanner(true);
    springApplicationBuilder.logStartupInfo(true);
    springApplicationBuilder.run(args);
  }
}
