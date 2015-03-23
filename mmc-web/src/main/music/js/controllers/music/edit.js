'use strict';

angular.module('mmcApp')
.controller('musicEditCtrl', ['$document', '$scope', '$rootScope', '$http', '$location', '$routeParams','userService', 'musicService', 'refValues', 'utils', 
function($document, $scope, $rootScope, $http, $location, $routeParams, userService, musicService, refValues, utils) {
 
 $scope.action = { 'result' : -1};
 $scope.selectedImages = [];
 $scope.fileItems = [];
 
 $scope.setImagesInTheScope = function() {
  if ($scope.doc.images != null && $scope.doc.images.length > 0) {
   for (var i=0; i < $scope.doc.images.length; i++) {
	$scope.selectedImages.push(false);
   }
  }
 };
 
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
  
 $scope.selectImage = function(index) {
  $scope.selectedImages[index] = !$scope.selectedImages[index];
  utils.debug(JSON.stringify($scope.selectedImages));
 };
 
 musicService.getDoc($routeParams.musicDocId, function(response) {
  $scope.action.resut = 0;
  $scope.doc = response;
  if ($scope.doc.images != null && $scope.doc.images.length > 0) {
   utils.debug($scope.doc.images.length + " photo(s)");
   for (var i=0; i < $scope.doc.images.length; i++) {
	$scope.selectedImages.push(false);
   }
  }
 }, function() {
  $scope.action.resut = 1;	 
 });

 $scope.update = function() {
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
  musicService.uploadPhoto(docId, fileItem.file, function(photo) {
   utils.debug('upload ok "' + fileItem.file.name + '"');
   fileItem.status='o';
   // MAJ LIST
   if ($scope.doc.images == null) {
    $scope.doc.images = [];   
   }
   $scope.doc.images.push(photo);
  }, function() {
   utils.debug('upload error "' + fileItem.file.name + '"'); 	 
   fileItem.status='e';
  });
 };
  
 $scope.removePhotos = function() {
  var photoIds = []; 
  for (var i=0; i <$scope.selectedImages.length; i++) {
   if ($scope.selectedImages[i]) {
	photoIds.push($scope.doc.images[i].id);
   }
  }
  
  if (photoIds.length > 0) {
   musicService.removePhotos($scope.doc.id, photoIds, function(doc) {
	utils.debug("removePhotos " + photoIds + " ok"); 
	$scope.doc = doc;
	$scope.setImagesInTheScope();
   }, function() {
	utils.error("removePhotos " + photoIds + " ko");
	$scope.setImagesInTheScope();
   });   
  } else {
   utils.debug("nothing to remove");
  }
 }
 
 $scope.gotoView = function() {
  $location.path('/music_view/' + $scope.doc.id);
 }
 
 $scope.gotoFind = function() {
  $location.path('/music_list');
 }; 
 
 $scope.remove = function(id) {
  var callback = function() {
   $location.path('/music_list');
  }
  musicService.remove(id, callback, callback);
 };  
 
}]);