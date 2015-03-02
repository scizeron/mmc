'use strict';

angular.module('mmcApp')
.controller('musicEditViewCtrl', ['$scope', '$rootScope', '$http', '$location', '$routeParams','userService', 'musicService', 'utils', 
function($scope, $rootScope, $http, $location, $routeParams, userService, musicService, utils) {
 
 $scope.action = { 'result' : -1};
 $scope.fileItems = [];
 
 $scope.grades = musicService.getGrades();
 $scope.countries = musicService.getCountries();
 $scope.nbTypeRange = musicService.getNbTypeRange();
 $scope.years = musicService.getYears();
 $scope.defaultMusicCountry = settings.music.defaultCountry;
 $scope.types = settings.music.types;
 
 musicService.getDoc($routeParams.musicDocId, function(response) {
  $scope.action.resut = 0;
  $scope.canEdit = userService.userHasRole("WRITE");
  $scope.doc = response;
  $scope.mainImage = response.sortedImages[0];
  $scope.images = response.sortedImages;  
 }, function() {
  $scope.action.resut = 1;	 
 });

 $scope.setImage = function(image) {
  $scope.mainImage = image;
 }; 
 
 $scope.reset = function() {
  musicService.getDoc($routeParams.musicDocId, function(response) {
   $scope.action.resut = 0;
   $scope.doc = response;
  }, function() {
   $scope.action.resut = 1;	  
  });
 };
 
 $scope.update = function() {
  utils.debug('update "' + $scope.doc.id + ' ' + JSON.stringify($scope.doc));
  musicService.updateDoc($scope.doc, function() {
	$scope.action.resut = 0;
    utils.debug($scope.doc.id + ' is updated ok');
   }, function() {
	$scope.action.resut = 1;
   });  
 };
 
 $scope.removeFile = function(fileItem) {
  utils.debug('remove "' + fileItem.file.name + '"'); 
  var i = $scope.fileItems.indexOf(fileItem);
  if(i != -1) {
   $scope.fileItems.splice(i, 1);
  }
  utils.debug('files :  ' + $scope.fileItems.length);
 };
 
 $scope.uploadFile = function(fileItem) { 
  // status 'i' : init, 'p' : progress, 'o' : ok, 'e' : error  
  var docId = $scope.doc.id;
  utils.debug('upload "' + fileItem.file.name + '"'); 	 
  utils.debug('doc: "' + docId + '"');
  fileItem.status='p';
  musicService.addImage(docId, fileItem.file, function() {
   utils.debug('upload ok "' + fileItem.file.name + '"');
   fileItem.status='o';
  }, function() {
   utils.debug('upload error "' + fileItem.file.name + '"'); 	 
   fileItem.status='e';
  });
 };
 
 $scope.gotoView = function() {
  $location.path('/music_view/' + $scope.doc.id);
 }
 
 $scope.gotoEdit = function() {
  $location.path('/music_edit/' + $scope.doc.id);
 };
 
 $scope.gotoFind = function() {
  $location.path('/music_list');
 }; 
 
}]);
