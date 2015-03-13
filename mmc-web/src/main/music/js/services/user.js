'use strict';

angular.module('mmcApp')
.factory('userService', ['$http', function($http) {

 function getUser() {
  var userInSession = webUtils.getSessionItem('user');	
  var user = null;
  if (userInSession != null) {
   webUtils.debug('The user is already in session');	
   user = new User(userInSession.firstName, userInSession.lastName, userInSession.roles);
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
  return user.hasRole(expectedRole);
 };
 
 function isAdmin() {
  return this.userHasRole('ADMIN');
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
   authenticatedUserCallback(new User(response.firstName, response.lastName, response.roles));
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
  isAdmin: isAdmin
 }
}]);