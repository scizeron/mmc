package com.stfciz.mmc.web.api.misc;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stfciz.mmc.web.api.AbstractApiController;
import com.stfciz.mmc.web.api.GetResponse;

/**
 * 
 * @author stfciz
 *
 * 16 juin 2015
 */
@RestController
@RequestMapping(value = "/misc", produces = { MediaType.APPLICATION_JSON_VALUE })
public class MiscController extends AbstractApiController<GetResponse, SaveRequest> {

}
