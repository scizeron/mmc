'use strict';

angular.module('mmcApp').service('imgService', ['$http', '$q', 'utils', function($http, $q, utils) {
 var count = 0;
 var pages = {};
 
 return {
  getPhotos: function(page, perPage, onSuccessCallback, onErrorCallack) {
   utils.debug('getPhotos (page: ' + page + ', perPage:' + perPage + ')');
   var uri = env.get('api.url') + '/photosets/appgallery?page=' + page + '&perPage=' + perPage + '&client_id=' + encodeURIComponent(env.get('oauth2.client_id'));
   $http.defaults.headers.common.Authorization = 'Bearer ' + webUtils.getSessionItem('oauth2.accessToken');
   $http.get(uri).success(function(response) {
     onSuccessCallback(response);
    }).error(function(data, status, headers, config) {
     onErrorCallack();
    })
  },
  incrPageCount: function(page) {
   var key  = 'p'+ page;
   if (!(key in pages)) {
    pages[key] = page;  
    this.count = this.count + 1;
   }
   return this.count;
  },
  decrPageCount: function() {
   this.count = this.count - 1;
  }, 
  getPageCount: function() {
   return this.count;	
  }  
 };
}]);