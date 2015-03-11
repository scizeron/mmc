package com.stfciz.mmc.web.api.music;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.flickr4java.flickr.FlickrException;
import com.stfciz.mmc.core.music.ImageComparator;
import com.stfciz.mmc.core.music.domain.Image;
import com.stfciz.mmc.core.music.domain.MusicDocument;
import com.stfciz.mmc.core.music.domain.Obi;
import com.stfciz.mmc.core.music.domain.Purchase;
import com.stfciz.mmc.core.music.domain.RecordCompany;

/**
 * 
 * @author stfciz
 *
 */
@Component
public class ApiConverter {

  /**
   * 
   * @param src
   * @return
   * @throws FlickrException 
   */
  public com.stfciz.mmc.web.api.Photo convertPhotoDomain(com.flickr4java.flickr.photos.Photo src) throws FlickrException {
    com.stfciz.mmc.web.api.Photo target = new com.stfciz.mmc.web.api.Photo();
    target.setId(src.getId());
    target.putUrl("t", src.getThumbnailUrl());
    target.putUrl("o", src.getOriginalUrl());
    target.putUrl("m", src.getMediumUrl());
    return target;
  }
  
  /**
   * 
   * @param src
   * @return
   */
  public MusicDocument convertNewMusicRequestIn(NewRequest src) {
    MusicDocument target = new MusicDocument();
    target.setModified(new Date());
    target.setId(UUID.randomUUID().toString());
    target.setTitle(src.getTitle());
    target.setArtist(src.getArtist());
    target.setComment(src.getComment());
    target.setPromo(src.isPromo());
    target.setEdition(src.getEdition());
    target.setIssue(src.getIssue());
    target.setMainType(src.getMainType());
    target.setNbType(src.getNbType());
    target.setPubNum(src.getPubNum());
    target.setPubTotal(src.getPubTotal());
    target.setSleeveGrade(src.getSleeveGrade());
    target.setRecordGrade(src.getRecordGrade());
    target.setOrigin(src.getOrigin());
    
    if (StringUtils.isNotBlank(src.getRecordCompany())) {
      RecordCompany rc = new RecordCompany();
      rc.setName(src.getRecordCompany());
      rc.setLabel(src.getLabel());
      target.setRecordCompany(rc);
    }
    
    if ("JP".equals(src.getOrigin()) && StringUtils.isNotBlank(src.getObiColor()) && StringUtils.isNotBlank(src.getObiPos())) {
      target.setObi(new Obi(src.getObiColor(), Obi.Orientation.valueOf(src.getObiPos())));
    }
    
    if (src.getPurchasePrice() != null) {
      Purchase purchase = new Purchase();
      target.setPurchase(purchase);
      purchase.setPrice(src.getPurchasePrice());
      purchase.setContext(src.getPurchaseContext());
      purchase.setDate(src.getPurchaseDate());
      purchase.setVendor(src.getPurchaseVendor());
    }
    
    return target;
  }
  
  /**
   * 
   * @param src
   * @return
   */
  public FindElement convertMusicDocumentToFindDocument(MusicDocument src) {
    FindElement target = new FindElement();
    target.setId(src.getId());
    target.setTitle(src.getTitle());
    target.setArtist(src.getArtist());
    target.setEdition(src.getEdition());
    target.setMainType(src.getMainType());
    target.setNbType(src.getNbType());
    target.setOrigin(src.getOrigin());
    target.setModified(src.getModified());
    return target;
  }
  
  /**
   * 
   * @param src
   * @return
   */
  public MusicDocument convertUpdateMusicRequestIn(UpdateRequest src) {
    MusicDocument target = convertNewMusicRequestIn(src);
    target.setId(src.getId());
    return target;
  }
  
  /**
   * 
   * @param src
   * @return
   */
  public GetResponse convertMusicDocumentToGetResponse(MusicDocument src) {
    GetResponse target = new GetResponse();
    target.setId(src.getId());
    target.setTitle(src.getTitle());
    target.setArtist(src.getArtist());
    target.setComment(src.getComment());
    target.setPromo(src.isPromo());
    target.setEdition(src.getEdition());
    target.setIssue(src.getIssue());
    target.setMainType(src.getMainType());
    target.setNbType(src.getNbType());
    target.setPubNum(src.getPubNum());
    target.setPubTotal(src.getPubTotal());
    target.setSleeveGrade(src.getSleeveGrade());
    target.setRecordGrade(src.getRecordGrade());
    target.setOrigin(src.getOrigin());
    
    if ("JP".equals(src.getOrigin()) && src.getObi() != null && src.getObi().getOrientation() != null) {
      target.setObiColor(src.getObi().getColor());
      target.setObiPos(src.getObi().getOrientation().getValue());
    }
    
    if (src.getPurchase() != null) {
      target.setPurchaseDate(src.getPurchase().getDate());
      target.setPurchaseContext(src.getPurchase().getContext());
      target.setPurchaseVendor(src.getPurchase().getVendor());     
    }
    
    if (src.getRecordCompany() != null) {
      target.setLabel(src.getRecordCompany().getLabel());
      target.setRecordCompany(src.getRecordCompany().getName());
    }
    
    if (src.getPrices() != null && ! src.getPrices().isEmpty()) {
      // TODO
    }
    
    if (src.getImages() != null && !src.getImages().isEmpty()) {
      List<Image> sortedImages = getSortedImages(src.getImages());
      for (Image image : sortedImages) {
       target.getImageUrls().add(image.getUrl());
      }
    }
    
    return target;
  }
  
  /**
   * 
   * @return
   */
  public List<Image> getSortedImages(List<Image> imgs) {
    if (imgs != null) {
      Collections.sort(imgs, ImageComparator.get());
    }
    return imgs;
  }
}
