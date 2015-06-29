package com.stfciz.mmc.web.api.music;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mangofactory.swagger.models.Collections;
import com.stfciz.mmc.core.music.domain.MusicDocument;
import com.stfciz.mmc.core.music.domain.Obi;
import com.stfciz.mmc.core.music.domain.RecordCompany;
import com.stfciz.mmc.core.music.domain.SideMatrix;
import com.stfciz.mmc.web.api.AbstractApiConverter;
import com.stfciz.mmc.web.api.photo.PhotoApiConverter;

/**
 * 
 * @author stfciz
 *
 */
@Component("musicApiConverter")
public class MusicApiConverter extends AbstractApiConverter<MusicDocument, GetResponse, NewRequest, UpdateRequest, FindElementResponse, FindResponse> {

  @Autowired
  public MusicApiConverter(PhotoApiConverter photoApiConverter) {
    super(photoApiConverter);
  }

  @Override
  public MusicDocument newDocument() {
    return new MusicDocument();
  }

  @Override
  public FindResponse newFindResponse() {
    return new FindResponse();
  }
  
  @Override
  public FindElementResponse newFindElementResponse() {
    return new FindElementResponse();
  }
  
  @Override
  public GetResponse newGetResponse() {
    return new GetResponse();
  }

  @Override
  protected MusicDocument populateSpecificInfosFromNewRequest(MusicDocument target, NewRequest request) {
    target.setTitle(capitalizeWords(request.getTitle()));
    target.setArtist(capitalizeWords(request.getArtist()));
    
    target.setMainType(request.getMainType());
    target.setNbType(request.getNbType());
    target.setSerialNumber(request.getSerialNumber());
    
    if (request.getMainType() != null && (request.getMainType().contains("LP") || request.getMainType().contains("EP"))) {
     target.setVinylColor(request.getVinylColor());
    }
    
    if (request.getSleeveGrade() != null && request.getSleeveGrade() > 0) {
     target.setSleeveRating(request.getSleeveGrade());
    }
    
    if (request.getRecordGrade() != null && request.getRecordGrade() > 0) {
      target.setRecordRating(request.getRecordGrade());
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
    
    if (request.getSideMatrixes() != null && ! request.getSideMatrixes().isEmpty()) {
      for (com.stfciz.mmc.web.api.music.SideMatrix srcSideMatrix : request.getSideMatrixes()) {
        SideMatrix targetSideMatrix = new SideMatrix();
        BeanUtils.copyProperties(srcSideMatrix, targetSideMatrix);
        target.getSideMatrixs().add(targetSideMatrix);
      }
    }
    
    if (request.getSongs() != null && ! request.getSongs().isEmpty()) {
      for (com.stfciz.mmc.web.api.music.Song srcSong : request.getSongs()) {
        com.stfciz.mmc.core.music.domain.Song targetSong = new com.stfciz.mmc.core.music.domain.Song();
        BeanUtils.copyProperties(srcSong, targetSong);
        target.getSongs().add(targetSong);
      }
    }
    
    return target;
  }
  
  /**
   * 
   * @param src
   * @param target
   */
  private void populateAbstractMusicBaseResponse(MusicDocument src, AbstractMusicBaseResponse target) {
    target.setTitle(src.getTitle());
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
  }

  @Override
  protected GetResponse populateGetResponseWithSpecificInfos(MusicDocument src, GetResponse target) {
    populateAbstractMusicBaseResponse(src, target);
    if (src.getSideMatrixs() != null && ! src.getSideMatrixs().isEmpty()) {
      for (com.stfciz.mmc.core.music.domain.SideMatrix srcSideMatrix : src.getSideMatrixs()) {
        com.stfciz.mmc.web.api.music.SideMatrix targetSideMatrix = new  com.stfciz.mmc.web.api.music.SideMatrix();
        BeanUtils.copyProperties(srcSideMatrix, targetSideMatrix);
        target.getSideMatrixes().add(targetSideMatrix);
      }
    }
    
    if (src.getSongs() != null && ! src.getSongs().isEmpty()) {
      for (com.stfciz.mmc.core.music.domain.Song srcSong : src.getSongs()) {
        com.stfciz.mmc.web.api.music.Song targetSong = new  com.stfciz.mmc.web.api.music.Song();
        BeanUtils.copyProperties(srcSong, targetSong);
        target.getSongs().add(targetSong);
      }
      target.getSongs().sort(new Comparator<Song>() {
        @Override
        public int compare(Song s1, Song s2) {
          if (s1.getDisc() == s2.getDisc()) {
            if (s1.getSide() == s2.getSide()) {
              return s1.getOrder() - s2.getOrder();
            }
            return s1.getSide() - s2.getSide();
          }
          return s1.getDisc() - s2.getDisc();
        }
      });
    }
    
    return target;
  }

  @Override
  protected MusicDocument populateSpecificInfosFromUpdateRequest(
      MusicDocument target, UpdateRequest request) {
    return populateSpecificInfosFromNewRequest(target, request);
  }

  @Override
  protected FindElementResponse populateFindElementResponseWithSpecificInfos(
      MusicDocument doc, FindElementResponse target) {
    populateAbstractMusicBaseResponse(doc, target);
    return target;
  }
}
