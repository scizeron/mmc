package com.stfciz.mmc.web.api.music;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author stfciz
 *
 */
@JsonInclude(Include.NON_NULL)
public class AbstractBaseResponse {

  private String id;
  private String title;
  private String artist;
  private boolean promo;
  private String origin;
  private String serialNumber;
  private Integer edition;
  private Integer issue;
  private String mainType;
  private String nbType;
  private Integer pubNum;
  private Integer pubTotal;
  private String recordCompany;
  private String label;
  private Integer sleeveGrade;
  private Integer recordGrade;
  private Date lastModified;

  public AbstractBaseResponse() {
    super();
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getArtist() {
    return this.artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public Integer getEdition() {
    return this.edition;
  }

  public void setEdition(Integer edition) {
    this.edition = edition;
  }

  public String getMainType() {
    return this.mainType;
  }

  public void setMainType(String mainType) {
    this.mainType = mainType;
  }

  public String getNbType() {
    return this.nbType;
  }

  public void setNbType(String nbType) {
    this.nbType = nbType;
  }

  public String getOrigin() {
    return this.origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public Integer getIssue() {
    return this.issue;
  }

  public void setIssue(Integer issue) {
    this.issue = issue;
  }

  public boolean isPromo() {
    return this.promo;
  }

  public void setPromo(boolean promo) {
    this.promo = promo;
  }

  public String getSerialNumber() {
    return this.serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public Integer getPubNum() {
    return this.pubNum;
  }

  public void setPubNum(Integer pubNum) {
    this.pubNum = pubNum;
  }

  public String getRecordCompany() {
    return this.recordCompany;
  }

  public void setRecordCompany(String recordCompany) {
    this.recordCompany = recordCompany;
  }

  public String getLabel() {
    return this.label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Integer getSleeveGrade() {
    return this.sleeveGrade;
  }

  public void setSleeveGrade(Integer sleeveGrade) {
    this.sleeveGrade = sleeveGrade;
  }

  public Integer getRecordGrade() {
    return this.recordGrade;
  }

  public void setRecordGrade(Integer recordGrade) {
    this.recordGrade = recordGrade;
  }

  public Integer getPubTotal() {
    return this.pubTotal;
  }

  public void setPubTotal(Integer pubTotal) {
    this.pubTotal = pubTotal;
  }

  public Date getLastModified() {
    return this.lastModified;
  }

  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

}