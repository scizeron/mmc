package com.stfciz.clt.music.api;

import java.util.ArrayList;
import java.util.List;

import com.stfciz.clt.music.domain.MusicDocument;

/**
 * 
 * @author ByTel
 * 
 */
public class MusicDocumentsOut {

  private int                 pageSize;
  
  private int                 page;

  private int                 totalPages;

  private boolean             next;

  private boolean             previous;

  private List<MusicDocument> docs;

  /**
   * @return the docs
   */
  public List<MusicDocument> getDocs() {
    if (this.docs == null) {
      this.docs = new ArrayList<>();
    }
    return this.docs;
  }

  /**
   * @param docs
   *          the docs to set
   */
  public void setDocs(List<MusicDocument> docs) {
    this.docs = docs;
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
   * @return the totalPages
   */
  public int getTotalPages() {
    return this.totalPages;
  }

  /**
   * @param totalPages
   *          the totalPages to set
   */
  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  /**
   * @return the next
   */
  public boolean isNext() {
    return this.next;
  }

  /**
   * @param next
   *          the next to set
   */
  public void setNext(boolean next) {
    this.next = next;
  }

  /**
   * @return the previous
   */
  public boolean isPrevious() {
    return this.previous;
  }

  /**
   * @param previous
   *          the previous to set
   */
  public void setPrevious(boolean previous) {
    this.previous = previous;
  }

  /**
   * @return the pageSize
   */
  public int getPageSize() {
    return this.pageSize;
  }

  /**
   * @param pageSize the pageSize to set
   */
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
}
