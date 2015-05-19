package com.stfciz.mmc.web.controller.book;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stfciz.mmc.web.controller.AbstractApiController;

@RestController
@RequestMapping(value = "/book", produces = { MediaType.APPLICATION_JSON_VALUE })
public class BookController extends AbstractApiController {

}
