'use strict';

angular.module('mmcApp').factory('musicService', ['$http', '$q', 'utils', function($http, $q, utils) {
  
 function getDocs(page, reload, onSuccessCallback, onErrorCallack) {
  
  utils.debug('active page: ' + (page+1));
  
  var uri = env.get('api.url') + '/music/md?p=' + page + '&s=' + settings.pageSize + '&client_id=' + encodeURIComponent(env.get('oauth2.client_id'));
  $http.defaults.headers.common.Authorization = 'Bearer ' + webUtils.getSessionItem('oauth2.accessToken');
  $http.get(uri).
   success(function(response) {
    var docs = response;
    utils.debug('musicService.getDocs('+page+','+reload+'): ' + JSON.stringify(docs));
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
  $http.defaults.headers.common.Authorization = 'Bearer ' + webUtils.getSessionItem('oauth2.accessToken');
  $http.post(env.get('api.url') + '/music/md'  + "?client_id=" + encodeURIComponent(env.get('oauth2.client_id')),JSON.stringify(doc)).
   success(function(data, status) {
	utils.debug('A new doc is created : ' + JSON.stringify(data));
	onSuccessCallback(data);
   }).error(function(data, status) {
	utils.error('create error, status: ' + status);
	onErrorCallack();
   });  
 };
 
 function updateDoc(doc, onSuccessCallback, onErrorCallack) {
  $http.defaults.headers.common.Authorization = 'Bearer ' + webUtils.getSessionItem('oauth2.accessToken');
  $http.post(env.get('api.url') + '/music/md/' + doc.id  + "?client_id=" + encodeURIComponent(env.get('oauth2.client_id')),JSON.stringify(doc)).
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
  
  var uri = env.get('api.url') + '/music/md' + '/' + id + '?client_id=' + encodeURIComponent(env.get('oauth2.client_id'));
  $http.defaults.headers.common.Authorization = 'Bearer ' + webUtils.getSessionItem('oauth2.accessToken');
  $http.get(uri).
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
  var uri = env.get('api.url') + '/music/md/' + id + '/photos';
  utils.debug('upload uri: ' + uri + ', file:' + file.name);
  var fd = new FormData();
  fd.append('file', file); 
  $http.post(uri + "?client_id=" + encodeURIComponent(env.get('oauth2.client_id')), fd, {
	transformRequest: angular.identity,
	headers: {
	  'Content-Type': undefined
	 ,'Authorization': 'Bearer ' + webUtils.getSessionItem('oauth2.accessToken')
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
  $http.defaults.headers.common.Authorization = 'Bearer ' + webUtils.getSessionItem('oauth2.accessToken');
  var removePhotosIn = { 'ids' : photoIds};
  $http.delete(env.get('api.url') + '/music/md/' + id + '/photos?client_id=' + encodeURIComponent(env.get('oauth2.client_id')), {
	  'data' : JSON.stringify(removePhotosIn)
	, 'headers': {
		'Content-Type': 'application/json'
	}  
  })
  	.success(function(doc, status){ putDocInCache(doc); onSuccessCallback(doc);})
  	.error(function(data, status, headers, config) { onErrorCallack();});
 }
 
 function remove(id, onSuccessCallback, onErrorCallack) {
  var uri = env.get('api.url') + '/music/md/' + id + "?client_id=" + encodeURIComponent(env.get('oauth2.client_id'));
  $http.defaults.headers.common.Authorization = 'Bearer ' + webUtils.getSessionItem('oauth2.accessToken');
  $http.delete(uri).success(function(status){clearCachedDoc(); onSuccessCallback();}).error(function(data, status, headers, config) { onErrorCallack();});
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
