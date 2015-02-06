package com.stfciz.clt.music.api;

/**
 * 
 * @author ByTel
 * 
 */
public class MusicDocumentGetIn {

  private int    page;

  private int    pageSize;

  private String value;

  private String id;

  /**
   * @return the value
   */
  public String getValue() {
    return this.value;
  }

  /**
   * @param value
   *          the value to set
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * @return the page
   */
  public int getPage() {
    return this.page;
  }

  /**
   * @param page
   *          the page to set
   */
  public void setPage(int page) {
    this.page = page;
  }

  /**
   * @return the pageSize
   */
  public int getPageSize() {
    return this.pageSize;
  }

  /**
   * @param pageSize
   *          the pageSize to set
   */
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * @return the id
   */
  public String getId() {
    return this.id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

}
