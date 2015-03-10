package com.stfciz.clt.photo;

/**
 * 
 * @author stfciz
 *
 */
public class PhotoException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -6419801358542230308L;

  public PhotoException(String msg, Throwable cause) {
    super(String.format("%s, cause : %s", msg, cause.getMessage()));
  }
  
  public PhotoException(Throwable cause) {
    super(cause.getMessage());
  }
}
