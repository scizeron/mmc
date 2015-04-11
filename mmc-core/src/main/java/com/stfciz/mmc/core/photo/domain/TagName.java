package com.stfciz.mmc.core.photo.domain;


/**
 * 
 * @author Bellevue
 *
 */
public enum TagName {
 
 DOC_ID("docid");
 
 private String value;
 
 
 
 /**
  * 
  * @param value
  */
 private TagName(String value) {
   this.value = value;
 }
 
 public String getValue() {
   return this.value;
 }

}
