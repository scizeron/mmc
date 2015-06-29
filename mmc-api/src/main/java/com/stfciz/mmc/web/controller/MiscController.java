package com.stfciz.mmc.web.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stfciz.mmc.core.misc.domain.MiscDocument;
import com.stfciz.mmc.web.api.misc.GetResponse;
import com.stfciz.mmc.web.api.misc.NewRequest;
import com.stfciz.mmc.web.api.misc.UpdateRequest;
import com.stfciz.mmc.web.api.misc.FindElementResponse;
import com.stfciz.mmc.web.api.misc.FindResponse;

/**
 * 
 * @author stfciz
 *
 * 16 juin 2015
 */
@RestController
@RequestMapping(value = "/misc", produces = { MediaType.APPLICATION_JSON_VALUE })
public class MiscController extends AbstractApiController<MiscDocument, GetResponse, NewRequest, UpdateRequest, FindElementResponse, FindResponse> {


}
