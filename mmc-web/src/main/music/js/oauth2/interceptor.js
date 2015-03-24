'use strict';

angular.module('mmcApp').factory('oauth2Interceptor', ['$q', '$injector', 'utils', function($q, $injector, utils) {  
 var myInterceptor = {
  request: function(config) {
   var apiCall = (config.url.indexOf('/music/md')  == 0)
   if (apiCall) {
	// REST api
	utils.debug(config.method + ' '+  config.url);
	config.url = env.get('api.url') + config.url;
	config.headers['Authorization'] = 'Bearer ' + webUtils.getSessionItem('oauth2.accessToken');
	if (config.url.indexOf('?') > 0) {
	 config.url += '&'
	} else {
	 config.url += '?'	
	}
	config.url += 'client_id=' + encodeURIComponent(env.get('oauth2.client_id'));
   }
   return config;
  },
  responseError: function(response) {
   utils.error('Intercept ' + response.status);	  
   if (response.status == 403) {
	utils.error('Clear session');		   
	webUtils.clearSession();   
   }
   return $q.reject(response);
  }
 };
 return myInterceptor;
}]);