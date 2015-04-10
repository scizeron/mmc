'use strict';

angular.module('mmcApp').service('photoService', ['$http', '$q', 'utils', function($http, $q, utils) {
 var count = 0;
 var pages = {};
 
 return {
  getPhotos: function(page, perPage, onSuccessCallback, onErrorCallack) {
   utils.debug('getPhotos (page: ' + page + ', perPage:' + perPage + ')');
   var uri = '/photosets/mmc';
   
   if (page != null && perPage != null) {
	uri += '&perPage=' + perPage;    
   } else if (perPage != null) {
	uri += '?perPage=' + perPage;  
   } else if (page != null) {
	uri += '?page=' + page;  
   }
      
   $http.get(uri).success(function(response) {
     onSuccessCallback(response);
    }).error(function(data, status, headers, config) {
     onErrorCallack();
    })
  }
 };
}]);