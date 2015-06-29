package com.stfciz.mmc.web.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stfciz.mmc.core.book.domain.BookDocument;
import com.stfciz.mmc.web.api.book.GetResponse;
import com.stfciz.mmc.web.api.book.NewRequest;
import com.stfciz.mmc.web.api.book.UpdateRequest;
import com.stfciz.mmc.web.api.music.FindElementResponse;
import com.stfciz.mmc.web.api.music.FindResponse;

/**
 * 
 * @author stfciz
 *
 * 16 juin 2015
 */
@RestController
@RequestMapping(value = "/book", produces = { MediaType.APPLICATION_JSON_VALUE })
public class BookController extends AbstractApiController<BookDocument, GetResponse, NewRequest, UpdateRequest, FindElementResponse, FindResponse> {


}
