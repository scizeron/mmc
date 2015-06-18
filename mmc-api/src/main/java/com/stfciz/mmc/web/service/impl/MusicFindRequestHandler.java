package com.stfciz.mmc.web.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component("musicFindRequestHandler")
public class MusicFindRequestHandler extends AbstractFindRequestHandler {

  @Override
  public Sort getSort(String index) {
    return new Sort(new Sort.Order(Sort.Direction.ASC, "artist"),
        new Sort.Order(Sort.Direction.ASC, "title"));
  }
  
}
