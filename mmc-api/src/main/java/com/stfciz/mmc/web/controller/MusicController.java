package com.stfciz.mmc.web.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stfciz.mmc.core.music.domain.MusicDocument;
import com.stfciz.mmc.web.api.music.FindElementResponse;
import com.stfciz.mmc.web.api.music.FindResponse;
import com.stfciz.mmc.web.api.music.GetResponse;
import com.stfciz.mmc.web.api.music.NewRequest;
import com.stfciz.mmc.web.api.music.UpdateRequest;

/**
 * 
 * @author stfciz
 *
 */
@RestController
@RequestMapping(value = "/music", produces = { MediaType.APPLICATION_JSON_VALUE })
public class MusicController extends AbstractApiController<MusicDocument, GetResponse, NewRequest, UpdateRequest, FindElementResponse, FindResponse> {

}