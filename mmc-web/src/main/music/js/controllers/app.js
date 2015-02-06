'use strict';

angular.module('mmcApp')
.controller('appCtrl', ['$scope', '$http', '$location', 'userService', 'musicService', 'utils', 
function ($scope, $http, $location, userService, musicService, utils) {
 webUtils.debug('appCtrl : ' + $location.path());	
 utils.verbose = settings.verbose;
 
 $scope.app = {};
 var app = $scope.app;
 app.jumbotron = {show: false};
 
 if ($location.path() == '/home') {
  app.jumbotron = {show: true};
 }
 
 $scope.$on('authenticated.user', function(event, user) {
  webUtils.debug('on "authenticated.user" : ' + user);	 
  $scope.user = user.firstName + ' ' + user.lastName; 
  $scope.navbarMenuUser = true;
  $scope.navbarMenuLogin = false;
  $scope.navbarMenuLogout = true;
 }); 
 
 $scope.$on('jumbotron.show', function(event, value) {
  webUtils.debug('on "jumbotron.show" : ' + value);	 
  $scope.app.jumbotron = {show: value};
 });
 
 var user = userService.getUser();
 if (user != null) { 
  $scope.user = user.firstName + ' ' + user.lastName; 
  $scope.navbarMenuUser = true;
  $scope.navbarMenuLogin = false;
  $scope.navbarMenuLogout = true;
 } else {
  $scope.navbarMenuUser = false;
  $scope.navbarMenuLogin = true;
  $scope.navbarMenuLogout = false;
 } 
 
 $scope.navIsActive = function (viewLocation) {
  return $location.path().lastIndexOf(viewLocation, 0) == 0; 
 };
 
 $scope.logout = function($http) {
  userService.logout();
 };
 
}]);