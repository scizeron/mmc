package com.stfciz.clt.photo;

import org.apache.commons.lang.StringUtils;

import com.stfciz.clt.music.api.MusicDocumentOut;
/**
 * 
 * @author ByTel
 *
 */
public final class FlickrUtils {

  private FlickrUtils() {
    /** EMPTY **/
  }
  
  /**
   * 
   * https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
   *  or
   * https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg 
   * or
   * https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{o-secret}_o.(jpg|gif|png)
   * 
   * s  petit carré 75x75
   * q large square 150x150
   * t miniature, côté le plus long de 100
   * m petit, côté le plus long de 240
   * n small, 320 on longest side
   * - moyen, côté le plus long de 500
   * z Moyen 640, côté le plus long de 640
   * c moyen 800, 800 sur la longueur
   * b grand, côté le plus long de 1 024*
   * h largeur 1 600, longueur 1 600
   * k largeur 2 048, longueur 2 048†
   * o image d'origine, jpg, gif ou png selon le format source
   * 
   * † Les photos de taille moyenne de 800, ainsi que celle de largeur 1 600 et 2 048 n'existent que depuis le 1er mars 2012.

   * ex : 
   * 2014-11-26 09:23:14.716 DEBUG [main] FlickrApiImpl - com.flickr4java.flickr.photos.Photo@633b0e85[
   *  squareSize=<null>,smallSize=com.flickr4java.flickr.photos.Size@5d825dd
   *  ,thumbnailSize=com.flickr4java.flickr.photos.Size@63aca62,mediumSize=com.flickr4java.flickr.photos.Size@279c6b30,largeSize=<null>
   *  ,large1600Size=<null>,large2048Size=<null>,originalSize=com.flickr4java.flickr.photos.Size@5b422
   *  ,squareLargeSize=<null>,small320Size=<null>,medium640Size=<null>,medium800Size=<null>,videoPlayer=<null>
   *  ,siteMP4=<null>,videoOriginal=<null>,mobileMP4=<null>,hdMP4=<null>
   *  ,id=15273496290,owner=com.flickr4java.flickr.people.User@77485325
   *  ,secret=b77e90caff
   *  ,farm=4
   *  ,server=3930
   *  ,favorite=false,license=,primary=false,title=,description=<null>,publicFlag=false
   *  ,friendFlag=false,familyFlag=false,dateAdded=<null>,datePosted=<null>,dateTaken=<null>
   *  ,lastUpdate=<null>,takenGranularity=<null>,permissions=<null>,editability=<null>,publicEditability=<null>
   *  ,comments=0,views=-1,rotation=-1,notes=[],tags=[],urls=[],iconServer=,iconFarm=
   *  ,url=https://flickr.com/photos/127939020@N03/15273496290,geoData=<null>,originalFormat=jpg
   *  ,originalSecret=,placeId=,media=,mediaStatus=,pathAlias=,originalWidth=0,originalHeight=0
   *  ,photoUrl=<null>,usage=<null>,hasPeople=false,locality=<null>,county=<null>,region=<null>
   *  ,country=<null>,stats=<null>] 
   */

  /**
   * 
   * @param url
   * @param format
   * @return
   */
  public static String getPhotoUrl(String url, String format) {
    if (url == null) {
      return null;
    }
    int posExt = StringUtils.lastIndexOf(url,'.');
    return url.substring(0, posExt -1) + format + url.substring(posExt);
  }

  /**
   * 
   * @param url
   * @return
   */
  public static String getThumbPhotoUrl(String url) {
    return getPhotoUrl(url, "t");
  }
  
  /**
   * 
   * @param url
   * @return
   */
  public static String getThumbPhotoUrl(MusicDocumentOut doc) {
    return getPhotoUrl(doc.getMainImageUrl(), "t");
  }
}