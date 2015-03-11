package com.stfciz.mmc.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.stfciz.mmc.web.oauth2.PermissionException;

/**
 * 
 * @author stfciz
 *
 */
@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
  
  @ExceptionHandler(Exception.class)
  public @ResponseBody ErrorRepresentation handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) {
    LOGGER.error("Handle exception >> ", exception);
    int responseStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
    ErrorRepresentation error = new ErrorRepresentation();
    error.setReason(exception.getMessage());
    
    if (exception instanceof PermissionException) {
      PermissionException permissionException = (PermissionException) exception;
      responseStatus = permissionException.getStatus();
    }
    
    response.setStatus(responseStatus);
    return error;
  }
}
