package com.stfciz.clt.web.oauth2;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author stfciz
 *
 */
@ResponseStatus(value=HttpStatus.UNAUTHORIZED) 
public class PermissionException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -3677449446131006528L;
  
  private int status;

  /**
   * 
   * @param msg
   */
  public PermissionException(String msg, int status) {
    super(msg);
    this.status = status;
  }

  /**
   * 
   * @return
   */
  public int getStatus() {
    return status;
  }
}
