'use strict';

angular.module('mmcApp')
.factory('userService', ['$http', function($http) {

 function getUser() {
  var user = webUtils.getSessionItem('user');	
  if (user != null) {
   webUtils.debug('The user is already in session');	
  } else {
   webUtils.debug('no session user');
  }
  return user;
 };

 function userHasRole(expectedRole) {
  var user = this.getUser();	
  if (user == null) {
   return false;
  }
  var hasRole = false;
  user.roles.forEach(function(role) {
   if (role == expectedRole) {
    hasRole = true;
   }
  });
  return hasRole;
 };
 
 function setUserInfosIfAbsent(expiresIn, authenticatedUserCallback) {
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
   authenticatedUserCallback(response);
   return response;
  }).
  error(function(data, status, headers, config) {
   webUtils.error('status: ' + status);
   if (status == 401) {
	this.logout();    
   }
  }); 
 };
 
 function logout() {
  webUtils.debug('logout ...');
  webUtils.removeSessionItem('oauth2.accessToken');
  webUtils.removeSessionItem('oauth2.userInfos');
  webUtils.removeSessionItem('user');
 }; 
 
 return {
  getUser: getUser,
  userHasRole: userHasRole,
  setUserInfosIfAbsent: setUserInfosIfAbsent,
  logout: logout,
 }
}]);