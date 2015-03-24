'use strict';

angular.module('mmcApp').service('userService', ['$http', function($http) {

 return {
  getUser: function() {
   var userInSession = webUtils.getSessionItem('user');	
   var user = null;
   if (userInSession != null) {
    webUtils.debug('The user is already in session');	
    user = new User(userInSession.firstName, userInSession.lastName, userInSession.roles);
   } else {
    webUtils.debug('no session user');
   }
   return user;
  },
  loggedInUser: function() {
   return this.getUser() != null;  
  },
  userHasRole: function(expectedRole) {
   var user = this.getUser();	
   if (user == null) {
    return false;
   }
   return user.hasRole(expectedRole);
  },
  loggedInAdminUser: function() {
   return this.userHasRole('ADMIN');
  },
  logout:function() {
   webUtils.clearSession();
  },
  setUserInfosIfAbsent: function(expiresIn, authenticatedUserCallback) {
   var user = this.getUser();	
   if (user != null) {
    return user;
   }	 
   var accessToken = webUtils.getSessionItem('oauth2.accessToken');	
   $http.defaults.headers.common.Authorization = 'Bearer ' + accessToken;
   $http.get(env.get('oauth2.url') + '/userinfo' + '?client_id=' + encodeURIComponent(env.get('oauth2.client_id'))).
    success(function(response) {
     webUtils.debug('set the user in session');	
     webUtils.setSessionItem('user', response, expiresIn);
     authenticatedUserCallback(new User(response.firstName, response.lastName, response.roles));
     return response;
    }).
	error(function(data, status, headers, config) {
	 webUtils.error('status: ' + status);
	 if (status == 401) {
	  logout();    
	 }
	}); 
  }
 };
}]);