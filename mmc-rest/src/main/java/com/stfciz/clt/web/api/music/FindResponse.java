package com.stfciz.clt.web.api.music;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author stfciz
 *
 */
public class FindResponse {
  private int pageSize;

  private int page;

  private int totalPages;

  private boolean next;

  private boolean previous;

  private List<FindElement> docs;

  public int getPageSize() {
    return this.pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getPage() {
    return this.page;
  }

  public void setPage(int page) {
    this.page = page;
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

  public List<FindElement> getDocs() {
    if (this.docs == null) {
      this.docs = new ArrayList<>();
    }
    return this.docs;
  }

  public void setDocs(List<FindElement> docs) {
    this.docs = docs;
  }
}
