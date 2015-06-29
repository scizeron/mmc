package com.stfciz.mmc.web.api.book;

import com.stfciz.mmc.web.api.AbstractBaseResponse;
/**
 * 
 * @author stfciz
 *
 * 16 juin 2015
 */
public abstract class AbstractBookBaseResponse extends AbstractBaseResponse {
 
  private String title;
  
  private String description;

  private String author;

  private Integer globalRating;

  /**
   * 
   */
  private String publisher;

  /**
   * 
   */
  private String distributer;

  /**
   * 
   */
  private String isbn;

  /**
   * 
   */
  private Integer nbPages;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public Integer getGlobalRating() {
    return globalRating;
  }

  public void setGlobalRating(Integer globalRating) {
    this.globalRating = globalRating;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getDistributer() {
    return distributer;
  }

  public void setDistributer(String distributer) {
    this.distributer = distributer;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public Integer getNbPages() {
    return nbPages;
  }

  public void setNbPages(Integer nbPages) {
    this.nbPages = nbPages;
  }
}
