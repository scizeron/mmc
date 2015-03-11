package com.stfciz.mmc.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author stfciz
 *
 */
@RestController
public class HomeController {

  @RequestMapping("/") 
  public String home() {
    return "ok";
  }
  
}
