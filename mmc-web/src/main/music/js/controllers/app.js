'use strict';

angular.module('mmcApp')
.controller('appCtrl', ['$rootScope', '$scope', '$location', 'userService', 'utils', 
function ($rootScope, $scope, $location, userService, utils) {
 webUtils.debug('appCtrl : ' + $location.path());	
 utils.verbose = settings.verbose;
 
 $scope.context = function(user, jumbotron) {
  $rootScope.app = {
	  loggedInUser : (user != null) ? true : false
	, loggedInAdminUser : (user != null) ? user.isAdmin() : false
	, jumbotron : (typeof(jumbotron) != 'undefined') ? jumbotron : true
  };
 };
 
 if ($location.path().lastIndexOf('/login', 0) == 0) {
  oauth2.start($location.absUrl(), settings.scopes, false, function(expiresIn) {
   userService.setUserInfosIfAbsent(expiresIn, function(user) {
    $scope.context(user);
   });
  });
 } else if ($location.path().lastIndexOf('/logout', 0) == 0) {
  userService.logout();
  $scope.context(null, true);
 }

 $scope.$on('user', function(event, user) {
  webUtils.debug('on "user", user : ' + user);	 
  $scope.context(user, false);
 }); 
 
 $scope.navIsActive = function (viewLocation) {
  return $location.path().lastIndexOf(viewLocation, 0) == 0; 
 };
 
}]);