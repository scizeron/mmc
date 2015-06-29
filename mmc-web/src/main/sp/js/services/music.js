'use strict';

angular.module('mmcApp').factory('musicService', ['$http', '$q', 'utils', function($http, $q, utils) {
  
 /**
  * 
  * @param query
  * @param page 0 1 2 ...
  * @param useCache true false
  * @param content json pdf
  * @param type music book misc
  * @param onSuccessCallback
  * @param onErrorCallack
  * @returns
  */
 function getDocs(query, page, useCache, type, content, onSuccessCallback, onErrorCallack) {
  utils.debug('type:' + type + ', page: ' + page + ', query: ' + query + ', size: ' + settings.pageSize + ', useCache : ' + useCache);
  var cacheKey = type + '_list_page_' + page;
  
  if (useCache) {
   var cachedResponse = sessionStorage.getItem(cacheKey);
   if (cachedResponse != null) {
	utils.debug(type + '.getDocs('+page+') found in cache with key "' + cacheKey + '"');
	onSuccessCallback(JSON.parse(cachedResponse));
	return;
   } else {
	utils.debug(type + '.getDocs('+page+') not found in cache with key "' + cacheKey + '"');  
   }
  }
  
  var uri = '/' + type + '?p=' + page + '&s=' + settings.pageSize + '&i=' + type;
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
     utils.debug(type + '.getDocs('+page+'): ' + JSON.stringify(response));
	}
    if (useCache) {
     utils.debug(type + '.getDocs('+page+') stored in cache with key "' + cacheKey + '"');
     sessionStorage.setItem(cacheKey, JSON.stringify(response));
     var pages = sessionStorage.getItem(type + '_list_pages');
     if (pages == null) {
      sessionStorage.setItem(type + '_list_pages', page); 	 
     } else {
      sessionStorage.setItem(type + '_list_pages', pages+','+page); 
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
  * @param type
  * @param onSuccessCallback
  * @param onErrorCallack
  * @returns
  */
 function addDoc(doc, type, onSuccessCallback, onErrorCallack) {
  if (doc.origin != 'JP' && type == 'music') {
	doc.obiPos = null;
	doc.obiColor = null;
  }
  $http.post('/' + type, JSON.stringify(doc)).
   success(function(data, status) {
	utils.debug('a new "' + type + '" doc is created : ' + JSON.stringify(data));
	onSuccessCallback(data);
   }).error(function(data, status) {
	utils.error('create "' + type + '" error, status: ' + status);
	onErrorCallack();
   });  
 };
 
 /**
  * 
  * @param doc
  * @param type
  * @param onSuccessCallback
  * @param onErrorCallack
  * @returns
  */
 function updateDoc(doc, type, onSuccessCallback, onErrorCallack) {
  $http.post('/' + type + '/' + doc.id, JSON.stringify(doc)).
   success(function(data, status) {
	utils.debug('update "' + type + '" ' + JSON.stringify(doc));
	clearCache(type);
	onSuccessCallback(data);
   }).error(function(data, status) {
	utils.error('update "' + type + '" error, status: ' + status);
	onErrorCallack();
   });  
 }; 
 
 /**
  * 
  * @param doc
  * @param type
  * @returns
  */
 function putDocInCache(doc, type) {
  utils.debug('put "' + doc.id + '" in cache (key:' + type + ')');
  sessionStorage.setItem(type, JSON.stringify(doc));
 }
 
 /**
  * 
  * @param id
  * @param type
  * @returns
  */
 function getCachedDoc(id, type) {
  var jsonItem = sessionStorage.getItem(type);
  if (jsonItem != null) {
   utils.debug('get' + id + '" in cache (key:' + type + ')');
   return {'doc': JSON.parse(jsonItem), 'json': jsonItem};
  }
  return null;
 }
 
 /**
  * @param type
  * @returns
  */
 function clearCache(type) {
  utils.debug('clear cache "' + type + '"'); 
  sessionStorage.removeItem(type);
  var list = sessionStorage.getItem(type + '_list_pages');
  if (list != null) {
   var pages = list.split(',');  
   for (var i=0; i < pages.length; i++) {
	sessionStorage.removeItem(type + '_list_page_' + pages[i]);  
   }	  
   sessionStorage.removeItem(type + '_list_pages');
  }
 }
 
 /**
  * 
  * @param id
  * @param type
  * @param onSuccessCallback
  * @param onErrorCallack
  * @returns
  */
 function getDoc(id, type, onSuccessCallback, onErrorCallack) {
  var item = getCachedDoc(id, type);
   
  if (item != null) {
   if (item.doc.id == id) {
    onSuccessCallback(item.doc);
    return;
   }
   sessionStorage.removeItem(type);
  }
  
  $http.get('/' + type + '/' + id).
   success(function(doc) {
	utils.debug('get: '+ JSON.stringify(doc)); 
	putDocInCache(doc, type);
	onSuccessCallback(doc);
   }).error(function(data, status, headers, config) {
    utils.error('get "' + type + '" error, status: ' + status);
    onErrorCallack();
   }
  );
 };
 
 /**
  * 
  * @param id
  * @param type
  * @param file
  * @param onSuccessCallback
  * @param onErrorCallack
  * @returns
  */
 function uploadPhoto(id, type, file, onSuccessCallback, onErrorCallack) {
  utils.debug('upload ' + file.name);
  var fd = new FormData();
  fd.append('file', file); 
  $http.post('/' + type + '/' + id + '/photos', fd, {
	transformRequest: angular.identity,
	headers: {
	  'Content-Type': undefined
	 }
   }).success(function(photo, status) {
	var doc = getCachedDoc(id, type).doc;
	if (doc.images == null) {
	 doc.images = [];	
	}
	doc.images.push(photo);
	putDocInCache(doc, type);
	onSuccessCallback(photo);
   }).error(function(status) {
	utils.error('status: ' + status);
	onErrorCallack();
   });  
 };
 
 /**
  * 
  * @param id
  * @param type
  * @param photoIds
  * @param onSuccessCallback
  * @param onErrorCallack
  * @returns
  */
 function removePhotos(id, type, photoIds, onSuccessCallback, onErrorCallack) {
  var removePhotosIn = { 'ids' : photoIds};
  $http.delete('/' + type + '/' + id + '/photos', {
	  'data' : JSON.stringify(removePhotosIn)
	, 'headers': {
		'Content-Type': 'application/json'
	}  
  })
  	.success(function(doc, status){ putDocInCache(doc, type); onSuccessCallback(doc);})
  	.error(function(data, status, headers, config) { onErrorCallack();});
 }
 
 /**
  * 
  * @param id
  * @param type
  * @param onSuccessCallback
  * @param onErrorCallack
  * @returns
  */
 function remove(id, type, onSuccessCallback, onErrorCallack) {
  $http.delete('/' + type + '/' + id).success(function(status){clearCache(type); onSuccessCallback();}).error(function(data, status, headers, config) { onErrorCallack();});
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