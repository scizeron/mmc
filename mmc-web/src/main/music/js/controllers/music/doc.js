'use strict';

angular.module('mmcApp')
.controller('musicEditViewCtrl', ['$scope', '$rootScope', '$http', '$location', '$routeParams','userService', 'musicService', 'refValues', 'utils', 
function($scope, $rootScope, $http, $location, $routeParams, userService, musicService, refValues, utils) {
 
 $scope.action = { 'result' : -1};
 
 if ($location.path().indexOf('/music_edit/') > -1) {
  $scope.fileItems = [];	 
  refValues.getCountriesPromise().then(function(data){
   $scope.countries = data;
  });
  refValues.getGradesPromise().then(function(data){
   $scope.grades = data;
  });
  $scope.nbTypeRange = refValues.getNbTypeRange();
  $scope.years = refValues.getYears();
  $scope.defaultMusicCountry = settings.music.defaultCountry;
  $scope.types = settings.music.types;
 }
 
 musicService.getDoc($routeParams.musicDocId, function(response) {
  $scope.action.resut = 0;
  $scope.canEdit = userService.userHasRole('WRITE');
  $scope.doc = response;
  if ($location.path().indexOf('/music_view/') > -1) {
   if (response.images != null && response.images.length > 0) {
    $scope.mainImage = response.images[0];
    $scope.images = response.images;
   }
   $scope.line1 = response.artist;
   $scope.line2 = '';
   $scope.line3 = '';
   $scope.line4 = '';
   
   $scope.line2 = appendToLine($scope.line2, response.issue);
   $scope.line2 = appendToLine($scope.line2, response.edition, function(value) {
	return 'ed. ' + value;   
   });
   $scope.line2 = appendToLine($scope.line2, response.origin);
   $scope.line2 = appendToLine($scope.line2, response.mainType, function(value) {
    if (response.nbType != null && response.nbType > 1) {
     if (response.nbType.indexOf('0') == 0) {
      return response.nbType.substring(1) + ' ' + value; 	 
     }
     return response.nbType + ' ' + value;      
    } else {
     return value;	
    }
   });
  
   $scope.line2 = appendToLine($scope.line2, response.promo, function(value) {
	return 'promo';   
   });
   
   $scope.line3 = appendToLine($scope.line3, response.recordCompany);
   $scope.line3 = appendToLine($scope.line3, response.label);
   
   $scope.line4 = appendToLine($scope.line4, response.serialNumber, function(value) {
	return 'N° ' + value;   
   });
   $scope.line4 = appendToLine($scope.line4, response.pubNum, function(value) {
	return 'Limited Edition : ' + value + '/' + response.pubTotal;   
   });
   
  }
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
  utils.debug('update "' + JSON.stringify($scope.doc));
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
  utils.debug('upload "' + fileItem.file.name + '", doc: "' + docId + '"');
  fileItem.status='p';
  musicService.uploadPhoto(docId, fileItem.file, function() {
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
