'use strict';

angular.module('mmcApp').factory('oauth2Interceptor', ['$q', '$injector', 'utils', function($q, $injector, utils) {  
 var myInterceptor = {
  request: function(config) {
   utils.debug(config.method + ' '+  config.url);
   if (config.url.indexOf('/') == 0) {
	// REST api
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