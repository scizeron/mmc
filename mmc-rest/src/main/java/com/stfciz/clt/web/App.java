package com.stfciz.clt.web;

import org.springframework.boot.builder.SpringApplicationBuilder;

import com.stfciz.clt.AppSpringCoreConfiguration;

/**
 * 
 * @author ByTel
 *
 */
public class App {

  public static void main(String[] args) {
    final SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();
    springApplicationBuilder.sources(AppSpringCoreConfiguration.class, AppSpringWebConfiguration.class);
    springApplicationBuilder.showBanner(true);
    springApplicationBuilder.logStartupInfo(true);
    springApplicationBuilder.run(args);
  }
}
