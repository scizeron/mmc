package com.stfciz.mmc.web.api.music;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.stfciz.mmc.core.music.domain.MusicDocument;
import com.stfciz.mmc.core.music.domain.Obi;
import com.stfciz.mmc.core.music.domain.PhotoMusicDocument;
import com.stfciz.mmc.core.music.domain.Purchase;
import com.stfciz.mmc.core.music.domain.RecordCompany;
import com.stfciz.mmc.core.photo.flickr.FlickrUtils;

/**
 * 
 * @author stfciz
 *
 */
@Component
public class MusicApiConverter {

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
    target.setPubNum(request.getPubNum());
    target.setPubTotal(request.getPubTotal());
    target.setSleeveGrade(request.getSleeveGrade());
    target.setRecordGrade(request.getRecordGrade());
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
    
    if (src.getPhotos() != null && src.getPhotos().size() >= 1) {
      target.setThumbImageUrl(FlickrUtils.getUrls(src.getPhotos().get(0)).get("t"));
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
    
    if (src.getPhotos() != null && !src.getPhotos().isEmpty()) {
      List<Map<String,String>> images = new ArrayList<>();
      for (PhotoMusicDocument photoMusicDocument : src.getPhotos()) {
        images.add(FlickrUtils.getUrls(photoMusicDocument));
      }
      target.setImages(images);
    }
    
    return target;
  }
}
