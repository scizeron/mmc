package com.stfciz.mmc.web.api.music;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stfciz.mmc.web.api.AbstractApiController;
import com.stfciz.mmc.web.api.music.SaveRequest;

/**
 * 
 * @author stfciz
 *
 */
@RestController
@RequestMapping(value = "/music", produces = { MediaType.APPLICATION_JSON_VALUE })
public class MusicController extends AbstractApiController<GetResponse, SaveRequest> {

}
