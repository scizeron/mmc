package com.stfciz.clt.music;

import java.io.File;

import com.stfciz.clt.music.api.MusicDocumentGetIn;
import com.stfciz.clt.music.api.MusicDocumentOut;
import com.stfciz.clt.music.api.MusicDocumentWithPhotosSaveIn;
import com.stfciz.clt.music.api.MusicDocumentWithPhotosSaveIns;
import com.stfciz.clt.music.api.MusicDocumentsOut;

/**
 * 
 * @author ByTel
 *
 */
public interface MusicDocumentManager {

  /**
   * 
   * @param md
   * @return
   * @throws MusicManagerException
   */
  MusicDocumentOut saveMusicDocumentWithPhotos(MusicDocumentWithPhotosSaveIn md) throws MusicManagerException;
  
  /**
   * 
   * @param ins
   * @throws MusicManagerException
   */
  void saveMultipleMusicDocumentWithPhotos(MusicDocumentWithPhotosSaveIns ins) throws MusicManagerException;
  
  /**
   * 
   * @param file
   * @throws MusicManagerException
   */
  void saveMultipleMusicDocumentWithPhotosFromFile(File file) throws MusicManagerException;
  
  /**
   * 
   * @param in
   * @return
   * @throws MusicManagerException
   */
  MusicDocumentsOut getMusicDocuments(MusicDocumentGetIn in) throws MusicManagerException;
  
  /**
   * 
   * @param id
   * @return
   * @throws MusicManagerException
   */
  MusicDocumentOut findById(String id) throws MusicManagerException;
  
  /**
   * 
   * @param galleryId
   * @param docId
   * @param bytes
   * @param filename
   * @param async
   * @throws MusicManagerException
   */
  void addPhoto(String galleryId, String docId, byte [] bytes, String filename, boolean async) throws MusicManagerException;

  /**
   * 
   * @param galleryId
   * @param bytes
   * @param filename
   * @param async
   * @throws MusicManagerException
   */
  void addPhoto(String galleryId, byte [] bytes, String filename, boolean async) throws MusicManagerException;
}