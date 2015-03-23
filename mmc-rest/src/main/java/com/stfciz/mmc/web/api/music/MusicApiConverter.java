package com.stfciz.mmc.web.api.music;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stfciz.mmc.core.music.domain.MusicDocument;
import com.stfciz.mmc.core.music.domain.Obi;
import com.stfciz.mmc.core.music.domain.Purchase;
import com.stfciz.mmc.core.music.domain.RecordCompany;
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
   * @param request
   * @return
   */
  public MusicDocument convertNewMusicRequestIn(NewRequest request) {
    MusicDocument target = new MusicDocument();
    
    target.setId(UUID.randomUUID().toString());
    target.setModified(new Date());
    
    target.setTitle(request.getTitle());
    target.setArtist(request.getArtist());
    target.setComment(request.getComment());
    target.setPromo(request.isPromo());
    target.setEdition(request.getEdition());
    target.setIssue(request.getIssue());
    target.setMainType(request.getMainType());
    target.setNbType(request.getNbType());
    target.setSerialNumber(request.getSerialNumber());
    target.setPubNum(request.getPubNum());
    target.setPubTotal(request.getPubTotal());
    target.setSleeveRating(request.getSleeveGrade());
    target.setRecordRating(request.getRecordGrade());
    target.setOrigin(request.getOrigin());
    
    if (StringUtils.isNotBlank(request.getRecordCompany())) {
      RecordCompany rc = new RecordCompany();
      rc.setName(request.getRecordCompany());
      rc.setLabel(request.getLabel());
      target.setRecordCompany(rc);
    }
    
    if ("JP".equals(request.getOrigin()) && StringUtils.isNotBlank(request.getObiColor()) && StringUtils.isNotBlank(request.getObiPos())) {
      target.setObi(new Obi(request.getObiColor(), Obi.Orientation.valueOf(request.getObiPos())));
    }
    
    if (request.getPurchasePrice() != null) {
      Purchase purchase = new Purchase();
      target.setPurchase(purchase);
      purchase.setPrice(request.getPurchasePrice());
      purchase.setContext(request.getPurchaseContext());
      purchase.setDate(request.getPurchaseDate());
      purchase.setVendor(request.getPurchaseVendor());
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
    target.setEdition(src.getEdition());
    target.setIssue(src.getIssue());
    target.setSerialNumber(src.getSerialNumber());
    target.setMainType(src.getMainType());
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
    
    if ("JP".equals(src.getOrigin()) && src.getObi() != null && src.getObi().getOrientation() != null) {
      target.setObiColor(src.getObi().getColor());
      target.setObiPos(src.getObi().getOrientation().getValue());
    }
    
    if (src.getPurchase() != null) {
      target.setPurchaseDate(src.getPurchase().getDate());
      target.setPurchaseContext(src.getPurchase().getContext());
      target.setPurchaseVendor(src.getPurchase().getVendor());     
    }
        
    if (src.getPrices() != null && ! src.getPrices().isEmpty()) {
      // TODO
    }
    
    target.setImages(this.photoApiConverter.convertToApiPhotos(src.getPhotos()));
   
    return target;
  }
}
