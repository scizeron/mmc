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
  webUtils.debug('on "authenticated.user" : ' + user.toString());	 
  $scope.userDisplay = user.firstName + ' ' + user.lastName;
  $scope.navbarMenuAdmin = user.isAdmin();
 }); 
 
 $scope.$on('jumbotron.show', function(event, value) {
  webUtils.debug('on "jumbotron.show" : ' + value);	 
  $scope.app.jumbotron = {show: value};
 });
 
 $scope.navIsActive = function (viewLocation) {
  return $location.path().lastIndexOf(viewLocation, 0) == 0; 
 };
 
}]);