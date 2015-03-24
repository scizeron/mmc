'use strict';

angular.module('mmcApp').factory('musicService', ['$http', '$q', 'utils', function($http, $q, utils) {
  
 function getDocs(query, page, onSuccessCallback, onErrorCallack) {
  utils.debug('active page: ' + (page+1) + ', query: ' + query + ', size: ' + settings.pageSize);
  var uri = '/music/md?p=' + page + '&s=' + settings.pageSize;
  if (query != null) {
   uri += '&q=' + encodeURIComponent(query);   
  }
  $http.get(uri).
   success(function(response) {
    var docs = response;
    utils.debug('musicService.getDocs('+page+'): ' + JSON.stringify(docs));
    onSuccessCallback(docs, page);
   }).error(function(data, status, headers, config) {
    utils.error('status: ' + status);
    onErrorCallack();
   }
  );
 };
 
 function addDoc(doc, onSuccessCallback, onErrorCallack) {
  if (doc.origin != 'JP') {
	doc.obiPos = null;
	doc.obiColor = null;
  }
  $http.post('/music/md', JSON.stringify(doc)).
   success(function(data, status) {
	utils.debug('A new doc is created : ' + JSON.stringify(data));
	onSuccessCallback(data);
   }).error(function(data, status) {
	utils.error('create error, status: ' + status);
	onErrorCallack();
   });  
 };
 
 function updateDoc(doc, onSuccessCallback, onErrorCallack) {
  $http.post('/music/md/' + doc.id, JSON.stringify(doc)).
   success(function(data, status) {
	utils.debug('update ' + JSON.stringify(doc));
	clearCachedDoc();
	onSuccessCallback(data.id);
   }).error(function(data, status) {
	utils.error('update error, status: ' + status);
	onErrorCallack();
   });  
 }; 
 
 function putDocInCache(doc) {
  sessionStorage.setItem('md', JSON.stringify(doc));
 }
 
 function getCachedDoc(id) {
  var jsonItem = sessionStorage.getItem('md');
  if (jsonItem != null) {
   return {'doc': JSON.parse(jsonItem), 'json': jsonItem};
  }
  return null;
 }
 
 function clearCachedDoc() {
  sessionStorage.removeItem('md');	 
 }
 
 function getDoc(id, onSuccessCallback, onErrorCallack) {
  var item = getCachedDoc(id);
   
  if (item != null) {
   if (item.doc.id == id) {
	utils.debug('get "' + id + '" in session cache : ' + item.json);
    onSuccessCallback(item.doc);
    return;
   }
   clearCachedDoc();
  }
  
  $http.get('/music/md/' + id).
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
 
 function uploadPhoto(id, file, onSuccessCallback, onErrorCallack) {
  utils.debug('upload ' + file.name);
  var fd = new FormData();
  fd.append('file', file); 
  $http.post('/music/md/' + id + '/photos', fd, {
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
 
 function removePhotos(id, photoIds, onSuccessCallback, onErrorCallack) {
  var removePhotosIn = { 'ids' : photoIds};
  $http.delete('/music/md/' + id + '/photos', {
	  'data' : JSON.stringify(removePhotosIn)
	, 'headers': {
		'Content-Type': 'application/json'
	}  
  })
  	.success(function(doc, status){ putDocInCache(doc); onSuccessCallback(doc);})
  	.error(function(data, status, headers, config) { onErrorCallack();});
 }
 
 function remove(id, onSuccessCallback, onErrorCallack) {
  $http.delete('/music/md/' + id).success(function(status){clearCachedDoc(); onSuccessCallback();}).error(function(data, status, headers, config) { onErrorCallack();});
 }
 
 return { 
  getDocs: getDocs,
  getDoc: getDoc,
  getCachedDoc : getCachedDoc,
  clearCachedDoc : clearCachedDoc,
  putDocInCache : putDocInCache,
  addDoc: addDoc,
  updateDoc: updateDoc,
  uploadPhoto: uploadPhoto,
  removePhotos : removePhotos,
  remove: remove
 }
 
}]);