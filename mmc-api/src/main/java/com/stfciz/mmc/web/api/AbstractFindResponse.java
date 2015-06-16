package com.stfciz.mmc.web.api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author stfciz
 *
 * 16 juin 2015
 */
@JsonInclude(Include.NON_NULL)
public class AbstractFindResponse<E extends AbstractBaseResponse> {

  @JsonProperty(value="docs")
  private List<E> items;
  
  private boolean next;
  
  private boolean previous;
  
  private int pageSize;
  
  private int totalPages;
  
  private int page;

  public AbstractFindResponse() {
    super();
  }

  public int getPageSize() {
    return this.pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public void setPage(int page) {
    this.page = page;
  }

  /**
   * 
   * @return
   */
  public int getPage() {
    return this.page;
  }

  public int getTotalPages() {
    return this.totalPages;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  public boolean isNext() {
    return this.next;
  }

  public void setNext(boolean next) {
    this.next = next;
  }

  public boolean isPrevious() {
    return this.previous;
  }

  public void setPrevious(boolean previous) {
    this.previous = previous;
  }

  public List<E> getItems() {
    if (this.items == null) {
      this.items = new ArrayList<E>();
    }
    return this.items;
  }

  public void setItems(List<E> docs) {
    this.items = docs;
  }

}