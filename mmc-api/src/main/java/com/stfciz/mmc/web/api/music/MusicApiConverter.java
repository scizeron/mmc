package com.stfciz.mmc.web.api.music;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stfciz.mmc.core.domain.MMCDocument;
import com.stfciz.mmc.core.domain.DocumentType;
import com.stfciz.mmc.core.domain.music.Obi;
import com.stfciz.mmc.core.domain.music.RecordCompany;
import com.stfciz.mmc.core.domain.music.SideMatrix;
import com.stfciz.mmc.web.api.AbstractApiConverter;
import com.stfciz.mmc.web.api.photo.PhotoApiConverter;

/**
 * 
 * @author stfciz
 *
 * 2 juil. 2015
 */
@Component("musicApiConverter")
public class MusicApiConverter extends AbstractApiConverter<GetResponse, SaveRequest> {

  @Autowired
  public MusicApiConverter(PhotoApiConverter photoApiConverter) {
    super(photoApiConverter);
  }

  @Override
  public GetResponse newGetResponse() {
    return new GetResponse();
  }

  @Override
  public DocumentType getDocumentType() {
    return DocumentType.MUSIC;
  }
  
  @Override
  protected MMCDocument populateFromSaveRequest(MMCDocument target, SaveRequest request) {
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
        targetSideMatrix.setValue(srcSideMatrix.getValue());
        targetSideMatrix.setIDisc(srcSideMatrix.getDisc());
        targetSideMatrix.setISide(srcSideMatrix.getSide());
        target.getSideMatrixs().add(targetSideMatrix);
      }
    }
    
    if (request.getSongs() != null && ! request.getSongs().isEmpty()) {
      for (com.stfciz.mmc.web.api.music.Song srcSong : request.getSongs()) {
        com.stfciz.mmc.core.domain.music.Song targetSong = new com.stfciz.mmc.core.domain.music.Song();
        BeanUtils.copyProperties(srcSong, targetSong);
        target.getSongs().add(targetSong);
      }
    }    
    return target;
  }

  @Override
  protected GetResponse populateGetResponseWithSpecificInfos(MMCDocument src, GetResponse target) {
    if (src.getSideMatrixs() != null && ! src.getSideMatrixs().isEmpty()) {
      for (com.stfciz.mmc.core.domain.music.SideMatrix srcSideMatrix : src.getSideMatrixs()) {
        com.stfciz.mmc.web.api.music.SideMatrix targetSideMatrix = new  com.stfciz.mmc.web.api.music.SideMatrix();
        //BeanUtils.copyProperties(srcSideMatrix, targetSideMatrix);
        targetSideMatrix.setValue(srcSideMatrix.getValue());
        targetSideMatrix.setSide(srcSideMatrix.getISide());
        targetSideMatrix.setDisc(srcSideMatrix.getIDisc());
        target.getSideMatrixes().add(targetSideMatrix);
      }
    }
    
    if (src.getSongs() != null && ! src.getSongs().isEmpty()) {
      for (com.stfciz.mmc.core.domain.music.Song srcSong : src.getSongs()) {
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
}
