package com.stfciz.mmc.web.api;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.stfciz.mmc.core.domain.AbstractDocument;
import com.stfciz.mmc.core.domain.Purchase;
import com.stfciz.mmc.web.api.photo.Photo;
import com.stfciz.mmc.web.api.photo.PhotoApiConverter;
/**
 * 
 * @author stfciz
 *
 * 16 juin 2015
 */
public abstract class AbstractApiConverter<D extends AbstractDocument, GR extends AbstractBaseResponse, NR extends AbstractNewRequest, UR extends AbstractNewRequest, FER extends AbstractBaseResponse, FR> {
  
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

  /**
   * 
   * @return
   */
  public abstract FR newFindResponse();
  
  /**
   * 
   * @return
   */
  public abstract FER newFindElementResponse();
  
  /**
   * 
   * @return
   */
  public abstract D newDocument();
  
  /**
   * 
   * @return
   */
  public abstract GR newGetResponse();
  
  /**
   * 
   * @param target
   * @param request
   * @return
   */
  protected abstract D populateSpecificInfosFromNewRequest(D target, NR request);
  
  /**
   * 
   * @param target
   * @param request
   * @return
   */
  protected abstract D populateSpecificInfosFromUpdateRequest(D target, UR request);
  
  /**
   * 
   * @param src
   * @param target
   * @return
   */
  protected AbstractBaseResponse populateAbstractBaseResponseFromDocument(D src, AbstractBaseResponse target) {
    target.setId(src.getId());
    target.setLastModified(src.getModified());
    target.setPromo(src.isPromo());
    target.setReEdition(src.isReEdition());
    target.setIssue(src.getIssue());
    target.setPubNum(src.getPubNum());
    target.setPubTotal(src.getPubTotal());
    target.setOrigin(src.getOrigin());
    target.setComment(src.getComment());
    target.setImages(getPhotoApiConverter().convertToApiPhotos(src.getPhotos()));
    
    if (src.getPurchase() != null) {
      target.setPurchasePrice(src.getPurchase().getPrice());
      target.setPurchaseMonth(src.getPurchase().getMonth());
      target.setPurchaseYear(src.getPurchase().getYear());
      target.setPurchaseContext(src.getPurchase().getContext());
      target.setPurchaseVendor(src.getPurchase().getVendor());     
    }
        
    if (src.getPrices() != null && ! src.getPrices().isEmpty()) {
      for (com.stfciz.mmc.core.music.domain.UpdatePrice updatePrice : src.getPrices()) {
        com.stfciz.mmc.web.api.UpdatePrice up = new com.stfciz.mmc.web.api.UpdatePrice();
        BeanUtils.copyProperties(updatePrice, up);
        target.getPrices().add(up);
      }
    }
    
    return target;
  }

  protected abstract GR populateGetResponseWithSpecificInfos(D doc, GR target);
  
  /**
   * 
   * @param doc
   * @param target
   * @return
   */
  protected abstract FER populateFindElementResponseWithSpecificInfos(D doc, FER target);
  
  /**
   * 
   * @param doc
   * @return
   */
  public GR convertToGetResponse(D doc) {
    GR target = newGetResponse();
    populateAbstractBaseResponseFromDocument(doc, target);
    return populateGetResponseWithSpecificInfos(doc, target);
  }
  
  /**
   * 
   * @param request
   * @return
   */
  public D convertNewRequestContentToDcoument(NR request) {
    D target = newDocument();
    populateDocumentFromNewRequest(target, request);
    target.setId(UUID.randomUUID().toString());
    return populateSpecificInfosFromNewRequest(target, request);
  }
  
  /**
   * 
   * @param target
   * @param request
   * @return
   */
  private D populateDocumentFromNewRequest(D target, AbstractNewRequest request) {
    target.setId(request.getId());
    target.setModified(new Date());
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
      
          com.stfciz.mmc.core.music.domain.UpdatePrice updatePriceTarget = new com.stfciz.mmc.core.music.domain.UpdatePrice();
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
  
  /**
   * 
   * @param request
   * @return
   */
  public D convertUpdateRequestContent(UR request) {
    return populateSpecificInfosFromUpdateRequest(populateDocumentFromNewRequest(newDocument(), request), request);
  }
  
  /**
   * 
   * @param src
   * @return
   */
  public FER convertToFindElementResponse(D src) {
    FER target = newFindElementResponse();
    populateAbstractBaseResponseFromDocument(src, target);
    populateFindElementResponseWithSpecificInfos(src,target);
    List<Photo> photos = getPhotoApiConverter().convertToApiPhotos(src.getPhotos());
    if (photos != null && photos.size() >= 1) {
      target.setThumbImageUrl(photos.get(0).getDetails().get("t").getUrl());
    };
    
    return target;
  }
 
}
