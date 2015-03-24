'use strict';

angular.module('mmcApp').factory('oauth2Interceptor', ['$q', '$injector', 'utils', function($q, $injector, utils) {  
 var myInterceptor = {
  responseError: function(response) {
   utils.error('Intercept ' + response.status);	  
   if (response.status == 403){
	utils.error('Clear session');		   
	webUtils.clearSession();   
   }
   return $q.reject(response);
  }
 };

 return myInterceptor;
}]);