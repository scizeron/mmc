package com.stfciz.mmc.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stfciz.mmc.web.api.FindResponse;
import com.stfciz.mmc.web.oauth2.OAuth2ScopeApi;
import com.stfciz.mmc.web.oauth2.Permission;
import com.stfciz.mmc.web.service.MMCService;

@RestController
@RequestMapping(value = "/search", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SearchController {
  
  @Autowired
  private MMCService mmcService;
  
  @RequestMapping(method = RequestMethod.GET
      , consumes = { MediaType.ALL_VALUE })
  @Permission(scopes = { OAuth2ScopeApi.READ })
  public FindResponse find(
      @RequestParam(value = "q", required = false) String query,
      @RequestParam(value = "p", required = false, defaultValue = "0") int page,
      @RequestParam(value = "s", required = false, defaultValue = "50") int pageSize) {
    
    return this.mmcService.search(query, page, pageSize, false, null);
  }
}
