'use strict';

angular.module('mmcApp').factory('musicService', ['$http', '$q', 'utils', function($http, $q, utils) {
  
 /**
  * 
  * @param query
  * @param page
  * @param useCache
  * @param content
  * @param onSuccessCallback
  * @param onErrorCallack
  * @returns
  */
 function getDocs(query, page, useCache, content, onSuccessCallback, onErrorCallack) {
  utils.debug('page: ' + page + ', query: ' + query + ', size: ' + settings.pageSize + ', useCache : ' + useCache);
  var cacheKey = 'music_list_page_' + page;
  
  if (useCache) {
   var cachedResponse = sessionStorage.getItem(cacheKey);
   if (cachedResponse != null) {
	utils.debug('musicService.getDocs('+page+') found in cache with key "' + cacheKey + '"');
	onSuccessCallback(JSON.parse(cachedResponse));
	return;
   } else {
	utils.debug('musicService.getDocs('+page+') not found in cache with key "' + cacheKey + '"');  
   }
  }
  
  var uri = '/music?p=' + page + '&s=' + settings.pageSize;
  if (query != null) {
   uri += '&q=' + encodeURIComponent(query);   
  }
  
  var config = null;
  if ('pdf' == content) {
   config = {headers: {'Accept': 'application/pdf'}, responseType: 'arraybuffer'};
  }
  
  $http.get(uri, config).
   success(function(response) {
	if ('json' == content) {	   
     utils.debug('musicService.getDocs('+page+'): ' + JSON.stringify(response));
	}
    if (useCache) {
     utils.debug('musicService.getDocs('+page+') stored in cache with key "' + cacheKey + '"');
     sessionStorage.setItem(cacheKey, JSON.stringify(response));
     var pages = sessionStorage.getItem('music_list_pages');
     if (pages == null) {
      sessionStorage.setItem('music_list_pages', page); 	 
     } else {
      sessionStorage.setItem('music_list_pages', pages+','+page); 
     }
    }
    onSuccessCallback(response);
   }).error(function(data, status, headers, config) {
    utils.error('status: ' + status);
    onErrorCallack();
   }
  );
 };
 
 /**
  * 
  * @param doc
  * @param onSuccessCallback
  * @param onErrorCallack
  * @returns
  */
 function addDoc(doc, onSuccessCallback, onErrorCallack) {
  if (doc.origin != 'JP') {
	doc.obiPos = null;
	doc.obiColor = null;
  }
  $http.post('/music', JSON.stringify(doc)).
   success(function(data, status) {
	utils.debug('A new doc is created : ' + JSON.stringify(data));
	onSuccessCallback(data);
   }).error(function(data, status) {
	utils.error('create error, status: ' + status);
	onErrorCallack();
   });  
 };
 
 /**
  * 
  * @param doc
  * @param onSuccessCallback
  * @param onErrorCallack
  * @returns
  */
 function updateDoc(doc, onSuccessCallback, onErrorCallack) {
  $http.post('/music/' + doc.id, JSON.stringify(doc)).
   success(function(data, status) {
	utils.debug('update ' + JSON.stringify(doc));
	clearCache();
	onSuccessCallback(data);
   }).error(function(data, status) {
	utils.error('update error, status: ' + status);
	onErrorCallack();
   });  
 }; 
 
 /**
  * 
  * @param doc
  * @returns
  */
 function putDocInCache(doc) {
  sessionStorage.setItem('md', JSON.stringify(doc));
 }
 
 /**
  * 
  * @param id
  * @returns
  */
 function getCachedDoc(id) {
  var jsonItem = sessionStorage.getItem('md');
  if (jsonItem != null) {
   return {'doc': JSON.parse(jsonItem), 'json': jsonItem};
  }
  return null;
 }
 
 /**
  * 
  * @returns
  */
 function clearCache() {
  utils.debug('clearCache'); 
  sessionStorage.removeItem('md');
  var list = sessionStorage.getItem('music_list_pages');
  if (list != null) {
   var pages = list.split(',');  
   for (var i=0; i < pages.length; i++) {
	sessionStorage.removeItem('music_list_page_' + pages[i]);  
   }	  
   sessionStorage.removeItem('music_list_pages');
  }
 }
 
 /**
  * 
  * @param id
  * @param onSuccessCallback
  * @param onErrorCallack
  * @returns
  */
 function getDoc(id, onSuccessCallback, onErrorCallack) {
  var item = getCachedDoc(id);
   
  if (item != null) {
   if (item.doc.id == id) {
	utils.debug('get "' + id + '" in session cache : ' + item.json);
    onSuccessCallback(item.doc);
    return;
   }
   sessionStorage.removeItem('md');
  }
  
  $http.get('/music/' + id).
   success(function(doc) {
	utils.debug('get: '+ JSON.stringify(doc)); 
	putDocInCache(doc);
	onSuccessCallback(doc);
   }).error(function(data, status, headers, config) {
    utils.error('get error, status: ' + status);
    onErrorCallack();
   }
  );
 };
 
 /**
  * 
  * @param id
  * @param file
  * @param onSuccessCallback
  * @param onErrorCallack
  * @returns
  */
 function uploadPhoto(id, file, onSuccessCallback, onErrorCallack) {
  utils.debug('upload ' + file.name);
  var fd = new FormData();
  fd.append('file', file); 
  $http.post('/music/' + id + '/photos', fd, {
	transformRequest: angular.identity,
	headers: {
	  'Content-Type': undefined
	 }
   }).success(function(photo, status) {
	var doc = getCachedDoc(id).doc;
	if (doc.images == null) {
	 doc.images = [];	
	}
	doc.images.push(photo);
	putDocInCache(doc);
	onSuccessCallback(photo);
   }).error(function(status) {
	utils.error('status: ' + status);
	onErrorCallack();
   });  
 };
 
 /**
  * 
  * @param id
  * @param photoIds
  * @param onSuccessCallback
  * @param onErrorCallack
  * @returns
  */
 function removePhotos(id, photoIds, onSuccessCallback, onErrorCallack) {
  var removePhotosIn = { 'ids' : photoIds};
  $http.delete('/music/' + id + '/photos', {
	  'data' : JSON.stringify(removePhotosIn)
	, 'headers': {
		'Content-Type': 'application/json'
	}  
  })
  	.success(function(doc, status){ putDocInCache(doc); onSuccessCallback(doc);})
  	.error(function(data, status, headers, config) { onErrorCallack();});
 }
 
 /**
  * 
  * @param id
  * @param onSuccessCallback
  * @param onErrorCallack
  * @returns
  */
 function remove(id, onSuccessCallback, onErrorCallack) {
  $http.delete('/music/' + id).success(function(status){clearCache(); onSuccessCallback();}).error(function(data, status, headers, config) { onErrorCallack();});
 }
 
 return { 
  getDocs: getDocs,
  getDoc: getDoc,
  getCachedDoc : getCachedDoc,
  clearCache : clearCache,
  putDocInCache : putDocInCache,
  addDoc: addDoc,
  updateDoc: updateDoc,
  uploadPhoto: uploadPhoto,
  removePhotos : removePhotos,
  remove: remove
 }
 
}]);