package com.stfciz.mmc.web.api.book;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stfciz.mmc.web.api.AbstractApiController;
import com.stfciz.mmc.web.api.GetResponse;
import com.stfciz.mmc.web.api.book.SaveRequest;

/**
 * 
 * @author stfciz
 *
 * 16 juin 2015
 */
@RestController
@RequestMapping(value = "/book", produces = { MediaType.APPLICATION_JSON_VALUE })
public class BookController  extends AbstractApiController<GetResponse, SaveRequest> {

}
