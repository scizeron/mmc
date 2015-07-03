package com.stfciz.mmc.web.api;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.stfciz.mmc.core.domain.MMCDocument;
import com.stfciz.mmc.core.domain.Purchase;
import com.stfciz.mmc.core.domain.DocumentType;
import com.stfciz.mmc.web.api.photo.Photo;
import com.stfciz.mmc.web.api.photo.PhotoApiConverter;
/**
 * 
 * @author stfciz
 *
 * 16 juin 2015
 */
public abstract class AbstractApiConverter<GR extends FindItemResponse, SR extends AbstractSaveRequest> implements ApiConverter<GR, SR> {
  
  private PhotoApiConverter photoApiConverter;
  
  /**
   * 
   * @param photoApiConverter
   */
  public AbstractApiConverter(PhotoApiConverter photoApiConverter) {
    this.photoApiConverter = photoApiConverter;
  }
  
  /**
   * 
   * @return
   */
  public PhotoApiConverter getPhotoApiConverter() {
    return this.photoApiConverter;
  }
  
  /**
   * 
   * @param str
   * @return
   */
  public static String capitalizeWords(String str) {
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

  @Override
  public abstract GR newGetResponse();
  

  @Override
  public abstract DocumentType getDocumentType();
  
  /**
   * 
   * @param target
   * @param request
   * @return
   */
  protected MMCDocument populateFromSaveRequest(MMCDocument target, SR request) {
    return target;
  }
  

  /**
   * 
   * @param doc
   * @param target
   * @return
   */
  protected GR populateGetResponseWithSpecificInfos(MMCDocument doc, GR target) {
    return target;
  }
  
  @Override
  public FindItemResponse convertToFindItemResponse(MMCDocument src) {
    FindItemResponse target = new FindItemResponse();
    populateFindItemResponse(src, target);
    List<Photo> photos = getPhotoApiConverter().convertToApiPhotos(src.getPhotos());
    if (photos != null && photos.size() >= 1) {
      target.setThumbImageUrl(photos.get(0).getDetails().get("t").getUrl());
    };
    return target;
  }

  
  /**
   * 
   * @param src
   * @param target
   * @return
   */
  protected FindItemResponse populateFindItemResponse(MMCDocument src, FindItemResponse target) {
    target.setId(src.getId());
    target.setType(src.getType().toLowerCase());
    target.setTitle(src.getTitle());
    target.setLastModified(src.getModified());
    target.setPromo(src.isPromo());
    target.setReEdition(src.isReEdition());
    target.setIssue(src.getIssue());
    target.setPubNum(src.getPubNum());
    target.setPubTotal(src.getPubTotal());
    target.setOrigin(src.getOrigin());
    target.setComment(src.getComment());
    target.setImages(getPhotoApiConverter().convertToApiPhotos(src.getPhotos()));
    target.setDescription(src.getDescription());
 
    if (src.getPurchase() != null) {
      target.setPurchasePrice(src.getPurchase().getPrice());
      target.setPurchaseMonth(src.getPurchase().getMonth());
      target.setPurchaseYear(src.getPurchase().getYear());
      target.setPurchaseContext(src.getPurchase().getContext());
      target.setPurchaseVendor(src.getPurchase().getVendor());     
    }
        
    if (src.getPrices() != null && ! src.getPrices().isEmpty()) {
      for (com.stfciz.mmc.core.domain.UpdatePrice updatePrice : src.getPrices()) {
        com.stfciz.mmc.web.api.UpdatePrice up = new com.stfciz.mmc.web.api.UpdatePrice();
        BeanUtils.copyProperties(updatePrice, up);
        target.getPrices().add(up);
      }
    }
    
    /** music **/
    target.setArtist(src.getArtist());
    target.setSerialNumber(src.getSerialNumber());

    target.setMainType(src.getMainType());
    target.setVinylColor(src.getVinylColor());
    target.setNbType(src.getNbType());
    target.setSleeveGrade(src.getSleeveRating());
    target.setRecordGrade(src.getRecordRating());

    if (src.getRecordCompany() != null) {
      target.setLabel(src.getRecordCompany().getLabel());
      target.setRecordCompany(src.getRecordCompany().getName());
    }
    
    if ("JP".equals(src.getOrigin()) && src.getObi() != null && src.getObi().getOrientation() != null) {
      target.setObiColor(src.getObi().getColor());
      target.setObiPos(src.getObi().getOrientation().getValue());
    }
    
    /** book  & misc **/
    target.setAuthor(src.getAuthor());
    target.setIsbn(src.getIsbn());
    target.setNbPages(src.getNbPages());
    target.setPublisher(src.getPublisher());
    target.setDistributer(src.getDistributer());
    
    target.setGlobalRating(src.getGlobalRating());
    
    return target;
  }
  
  /**
   * 
   * @param target
   * @param request
   * @return
   */
  private MMCDocument populateFromAbstractNewRequest(MMCDocument target, SR request) {
    target.setId(request.getId());
    target.setTitle(capitalizeWords(request.getTitle()));
    target.setType(getDocumentType().name());
    target.setModified(new Date());
    target.setDescription(request.getDescription());
    target.setComment(request.getComment());
    target.setPubNum(request.getPubNum());
    target.setPubTotal(request.getPubTotal());
    target.setPromo(request.isPromo());
    target.setReEdition(request.getReEdition() != null ? request.getReEdition().booleanValue() : false);
    target.setIssue(request.getIssue());
    if (StringUtils.isNotBlank(request.getOrigin()) && ! "null".equals(request.getOrigin())) {
      target.setOrigin(request.getOrigin());
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
    
    if (request.getPrices() != null && ! request.getPrices().isEmpty()) {
      Integer mostUpdatedPrice = null;
      UpdatePrice previous = null;
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
      
          com.stfciz.mmc.core.domain.UpdatePrice updatePriceTarget = new com.stfciz.mmc.core.domain.UpdatePrice();
          BeanUtils.copyProperties(currentPrice, updatePriceTarget);
          target.getPrices().add(updatePriceTarget);
        }        
      }
      
      if (mostUpdatedPrice != null )  {
         target.setMostUpdatedPrice(mostUpdatedPrice);  
      }
    }
    
    return target;
  }


  /* (non-Javadoc)
   * @see com.stfciz.mmc.web.api.ApiConverter#convertToGetResponse(com.stfciz.mmc.core.domain.MMCDocument)
   */
  @Override
  public GR convertToGetResponse(MMCDocument doc) {
    GR target = newGetResponse();
    populateFindItemResponse(doc, target);
    return populateGetResponseWithSpecificInfos(doc, target);
  }
  
  /* (non-Javadoc)
   * @see com.stfciz.mmc.web.api.ApiConverter#convertFromNewRequest(SR)
   */
  @Override
  public MMCDocument convertFromNewRequest(SR request) {
    MMCDocument target = new MMCDocument();
    populateFromAbstractNewRequest(target, request);
    target.setId(UUID.randomUUID().toString());
    return populateFromSaveRequest(target, request);
  }
  
  /* (non-Javadoc)
   * @see com.stfciz.mmc.web.api.ApiConverter#convertFromUpdateRequest(SR)
   */
  @Override
  public MMCDocument convertFromUpdateRequest(SR request) {
    return populateFromSaveRequest(populateFromAbstractNewRequest(new MMCDocument(), request), request);
  }
 
}
