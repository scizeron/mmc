package com.stfciz.mmc.core.book.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import com.stfciz.mmc.core.domain.AbstractDocument;

/**
 * 
 * @author Bellevue
 *
 */
@Document(indexName = "book", type = "bd")
public class BookDocument extends AbstractDocument {

  private String title;

  private String description;

  private String author;

  /** uses the Goldmine Standard code **/
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
  private int nbPages;

  /**
   * 
   * @return
   */
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
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

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDistributer() {
    return distributer;
  }

  public void setDistributer(String distributer) {
    this.distributer = distributer;
  }

  public int getNbPages() {
    return nbPages;
  }

  public void setNbPages(int nbPages) {
    this.nbPages = nbPages;
  }

}
