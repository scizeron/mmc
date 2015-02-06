'use strict';

angular.module('mmcApp')
.factory('musicService', ['$http', 'utils', function($http, utils) {
 var docs = null;
 var selectedPage = -1;
 var countries = null;
 var nbTypeRange = null;
 var years = null;
 
 function getYears() {
  if (years != null) {
	return years;  
  }
  var minYear = settings.music.minYear; 
  var maxYear = new Date().getFullYear();
  years = [];
  for (var j=minYear; j <= maxYear; j++) {
   years.push(j);	 
  }
  return years;
 };
 
 function getNbTypeRange() {
  if (nbTypeRange != null) {
	return nbTypeRange;  
  }	
  var nbTypeRange = [];
  for (var i=1; i < settings.music.nbMaxType; i++) {
   if ( i < 10) {
	nbTypeRange.push("0" + i);	 
   } else {
    nbTypeRange.push(i);
   }
  }  
  return nbTypeRange;
 };
 
 function getCountries() {
  if (countries != null) {
   return countries;	  
  }
  $http.get("assets/country.json").success(function(response) {
   countries = response;	 
  });
  return countries;
 };
 
 function getDocs(page, reload, onSuccessCallback, onErrorCallack) {
  if( typeof(page) == 'undefined') {
   if (selectedPage == -1) {
    selectedPage = 0;
   }
  } else {
   selectedPage = page;  
  };	 
  
  utils.debug('active page: ' + (selectedPage+1));
  
  var uri = env.get('api.url') + '/music/md?p=' + selectedPage + '&s=' + settings.pageSize + '&client_id=' + encodeURIComponent(env.get('oauth2.client_id'));
  $http.defaults.headers.common.Authorization = 'Bearer ' + webUtils.getSessionItem('oauth2.accessToken');
  $http.get(uri).
   success(function(response) {
    docs = response;
    utils.debug('musicService.getDocs('+selectedPage+','+reload+'): ' + JSON.stringify(docs));
    onSuccessCallback(docs, selectedPage);
   }).error(function(data, status, headers, config) {
    utils.error('status: ' + status);
    onErrorCallack();
   }
  );
 };
 
 function addDoc(doc, onSuccessCallback, onErrorCallack) {
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
	onSuccessCallback(data.id);
   }).error(function(data, status) {
	utils.error('update error, status: ' + status);
	onErrorCallack();
   });  
 }; 
 
 function getDoc(id, onSuccessCallback, onErrorCallack) {
  var uri = env.get('api.url') + '/music/md' + '/' + id + '?client_id=' + encodeURIComponent(env.get('oauth2.client_id'));
  $http.defaults.headers.common.Authorization = 'Bearer ' + webUtils.getSessionItem('oauth2.accessToken');
  $http.get(uri).
   success(function(response) {
	utils.debug('get: '+ JSON.stringify(response)); 
    onSuccessCallback(response);
   }).error(function(data, status, headers, config) {
    utils.error('get error, status: ' + status);
    onErrorCallack();
   }
  );
 };
 
 function addImage(id, image, onSuccessCallback, onErrorCallack) {
  var uri = env.get('api.url') + '/music/md' + '/' + id + '/images';
  utils.debug('upload uri: ' + uri);
  var fd = new FormData();
  fd.append('file', image); 
  $http.post(uri + "?client_id=" + encodeURIComponent(env.get('oauth2.client_id')), fd, {
	  transformRequest: angular.identity,
	  headers: {
	   'Content-Type': undefined
	  ,'Authorization': 'Bearer ' + webUtils.getSessionItem('oauth2.accessToken')
	  }
   }).success(function(status) {
	onSuccessCallback();
   }).error(function(status) {
	utils.error('status: ' + status);
	onErrorCallack();
   });  
 };
 
 return { 
  getDocs: getDocs,
  getDoc: getDoc,
  addDoc: addDoc,
  updateDoc: updateDoc,
  getCountries : getCountries,
  getNbTypeRange : getNbTypeRange,
  getYears : getYears,
  addImage: addImage
 }
 
}]);
