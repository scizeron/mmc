package com.stfciz.mmc.web.api.music;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stfciz.mmc.core.domain.Purchase;
import com.stfciz.mmc.core.music.domain.MusicDocument;
import com.stfciz.mmc.core.music.domain.Obi;
import com.stfciz.mmc.core.music.domain.RecordCompany;
import com.stfciz.mmc.core.music.domain.SideMatrix;
import com.stfciz.mmc.web.api.photo.Photo;
import com.stfciz.mmc.web.api.photo.PhotoApiConverter;

/**
 * 
 * @author stfciz
 *
 */
@Component
public class MusicApiConverter {

  private PhotoApiConverter photoApiConverter;
  
  @Autowired
  public MusicApiConverter( PhotoApiConverter photoApiConverter) {
    this.photoApiConverter = photoApiConverter;
  }
  
  /**
   * 
   * @param str
   * @return
   */
  private String capitalizeWords(String str) {
    if (StringUtils.isBlank(str)) {
      return str;
    }
    StringBuilder result = new StringBuilder();
    String [] words = StringUtils.split(str);
    for (String word : words) {
      if (result.length() > 0) {
        result.append(" ");
      }
      result.append(StringUtils.capitalize(word.toLowerCase()));
    }
    return result.toString();
  }
  
  /**
   * 
   * @param request
   * @return
   */
  public MusicDocument convertNewMusicRequestIn(NewRequest request) {
    MusicDocument target = new MusicDocument();
    
    target.setId(UUID.randomUUID().toString());
    target.setModified(new Date());
    
    target.setTitle(capitalizeWords(request.getTitle()));
    target.setArtist(capitalizeWords(request.getArtist()));
    target.setComment(request.getComment());
    target.setPromo(request.isPromo());
    target.setReEdition(request.getReEdition() != null ? request.getReEdition().booleanValue() : false);
    target.setIssue(request.getIssue());
    target.setMainType(request.getMainType());
    target.setNbType(request.getNbType());
    if (request.getMainType() != null && (request.getMainType().contains("LP") || request.getMainType().contains("EP"))) {
     target.setVinylColor(request.getVinylColor());
    }
    target.setSerialNumber(request.getSerialNumber());
    target.setPubNum(request.getPubNum());
    target.setPubTotal(request.getPubTotal());
    if (request.getSleeveGrade() != null && request.getSleeveGrade() > 0) {
     target.setSleeveRating(request.getSleeveGrade());
    }
    if (request.getRecordGrade() != null && request.getRecordGrade() > 0) {
      target.setRecordRating(request.getRecordGrade());
    }
    
    if (StringUtils.isNotBlank(request.getOrigin()) && ! "null".equals(request.getOrigin())) {
     target.setOrigin(request.getOrigin());
    }
    
    if (StringUtils.isNotBlank(request.getRecordCompany())) {
      RecordCompany rc = new RecordCompany();
      rc.setName(request.getRecordCompany().toUpperCase());
      rc.setLabel(request.getLabel() != null ? request.getLabel().toUpperCase() : null);
      target.setRecordCompany(rc);
    }
    
    if ("JP".equals(request.getOrigin()) && StringUtils.isNotBlank(request.getObiColor()) && StringUtils.isNotBlank(request.getObiPos())) {
      target.setObi(new Obi(request.getObiColor(), Obi.Orientation.fromValue(request.getObiPos())));
    }
    
    if (request.getPurchasePrice() != null) {
      Purchase purchase = new Purchase();
      target.setPurchase(purchase);
      purchase.setPrice(request.getPurchasePrice());
      purchase.setContext(request.getPurchaseContext());
      purchase.setMonth(request.getPurchaseMonth());
      purchase.setYear(request.getPurchaseYear());
      purchase.setVendor(request.getPurchaseVendor());
      target.setMostUpdatedPrice(request.getPurchasePrice()); 
    }
    
    if (request.getSideMatrixes() != null && ! request.getSideMatrixes().isEmpty()) {
      for (com.stfciz.mmc.web.api.music.SideMatrix srcSideMatrix : request.getSideMatrixes()) {
        SideMatrix targetSideMatrix = new SideMatrix();
        BeanUtils.copyProperties(srcSideMatrix, targetSideMatrix);
        target.getSideMatrixs().add(targetSideMatrix);
      }
    }
    
    return target;
  }
  
  /**
   * 
   * @param target
   * @param src
   */
  private void populateAbstractBaseResponseFromMusicDocument(AbstractBaseResponse target, MusicDocument src) {
    target.setLastModified(src.getModified());
    target.setId(src.getId());
    target.setTitle(src.getTitle());
    target.setArtist(src.getArtist());
    target.setPromo(src.isPromo());
    target.setReEdition(src.isReEdition());
    target.setIssue(src.getIssue());
    target.setSerialNumber(src.getSerialNumber());
    target.setMainType(src.getMainType());
    target.setVinylColor(src.getVinylColor());
    target.setNbType(src.getNbType());
    target.setPubNum(src.getPubNum());
    target.setPubTotal(src.getPubTotal());
    target.setSleeveGrade(src.getSleeveRating());
    target.setRecordGrade(src.getRecordRating());
    target.setOrigin(src.getOrigin());
    if (src.getRecordCompany() != null) {
      target.setLabel(src.getRecordCompany().getLabel());
      target.setRecordCompany(src.getRecordCompany().getName());
    }
    if ("JP".equals(src.getOrigin()) && src.getObi() != null && src.getObi().getOrientation() != null) {
      target.setObiColor(src.getObi().getColor());
      target.setObiPos(src.getObi().getOrientation().getValue());
    }
  }
  
  /**
   * 
   * @param src
   * @return
   */
  public FindElementResponse convertMusicDocumentToFindDocument(MusicDocument src) {
    FindElementResponse target = new FindElementResponse();
    populateAbstractBaseResponseFromMusicDocument(target, src);

    List<Photo> photos = this.photoApiConverter.convertToApiPhotos(src.getPhotos());
    
    if (photos != null && photos.size() >= 1) {
      target.setThumbImageUrl(photos.get(0).getDetails().get("t").getUrl());
    };
    
    return target;
  }
  
  /**
   * 
   * @param request
   * @return
   */
  public MusicDocument convertUpdateMusicRequestIn(UpdateRequest request) {
    MusicDocument target = convertNewMusicRequestIn(request);
    target.setId(request.getId());
    Integer mostUpdatedPrice = null;
    UpdatePrice previous = null;

    // + prices
    for (UpdatePrice currentPrice : request.getPrices()) {
      if (currentPrice.getPrice() != null && currentPrice.getMonth()!= null && currentPrice.getYear() != null) {
        if (previous != null) {
          if (currentPrice.getYear() > previous.getYear()) {
            mostUpdatedPrice = currentPrice.getPrice(); 
          } else if (currentPrice.getYear().equals(previous.getYear()) && currentPrice.getMonth() > previous.getMonth()) {
            mostUpdatedPrice = currentPrice.getPrice();
          }
        } else {
          mostUpdatedPrice = currentPrice.getPrice();
        }
        
        previous = currentPrice;
        
        com.stfciz.mmc.core.music.domain.UpdatePrice updatePriceTarget = new com.stfciz.mmc.core.music.domain.UpdatePrice();
        BeanUtils.copyProperties(currentPrice, updatePriceTarget);
        target.getPrices().add(updatePriceTarget);
      }
    }
    
    target.setMostUpdatedPrice(mostUpdatedPrice);
    
    return target;
  }
  
  /**
   * 
   * @param src
   * @return
   */
  public GetResponse convertMusicDocumentToGetResponse(MusicDocument src) {
    GetResponse target = new GetResponse();
    populateAbstractBaseResponseFromMusicDocument(target, src);
    
    target.setComment(src.getComment());
    
    if (src.getPurchase() != null) {
      target.setPurchasePrice(src.getPurchase().getPrice());
      target.setPurchaseMonth(src.getPurchase().getMonth());
      target.setPurchaseYear(src.getPurchase().getYear());
      target.setPurchaseContext(src.getPurchase().getContext());
      target.setPurchaseVendor(src.getPurchase().getVendor());     
    }
        
    if (src.getPrices() != null && ! src.getPrices().isEmpty()) {
      for (com.stfciz.mmc.core.music.domain.UpdatePrice updatePrice : src.getPrices()) {
        com.stfciz.mmc.web.api.music.UpdatePrice up = new com.stfciz.mmc.web.api.music.UpdatePrice();
        BeanUtils.copyProperties(updatePrice, up);
        target.getPrices().add(up);
      }
    }
    
    if (src.getSideMatrixs() != null && ! src.getSideMatrixs().isEmpty()) {
      for (com.stfciz.mmc.core.music.domain.SideMatrix srcSideMatrix : src.getSideMatrixs()) {
        com.stfciz.mmc.web.api.music.SideMatrix targetSideMatrix = new  com.stfciz.mmc.web.api.music.SideMatrix();
        BeanUtils.copyProperties(srcSideMatrix, targetSideMatrix);
        target.getSideMatrixes().add(targetSideMatrix);
      }
    }
    
    target.setImages(this.photoApiConverter.convertToApiPhotos(src.getPhotos()));
   
    return target;
  }
}
