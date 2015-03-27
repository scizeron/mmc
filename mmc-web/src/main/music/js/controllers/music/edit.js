'use strict';

angular.module('mmcApp')
.controller('musicEditCtrl', ['$document', '$scope', '$rootScope', '$http', '$location', '$routeParams'
                               ,'userService', 'musicService', 'refValues', 'utils', 'appService', 
function($document, $scope, $rootScope, $http, $location, $routeParams
		, userService, musicService, refValues, utils, appService) {
 
 /**
  * 
  */
 $scope.initSelectedImages = function() {
  if ($scope.doc.images != null && $scope.doc.images.length > 0) {
   utils.debug($scope.doc.images.length + " photo(s)");
   for (var i=0; i < $scope.doc.images.length; i++) {
	$scope.selectedImages.push(false);
   }
  }
 };
	
 /**
  * 
  */
 $scope.edit = function(docId) {
  $scope.action = { 'result' : -1};
  $scope.countries = refValues.getCountries();
  $scope.grades = refValues.getGrades();
  $scope.nbTypeRange = refValues.getNbTypeRange();
  $scope.years = refValues.getYears();
  $scope.defaultMusicCountry = settings.music.defaultCountry;
  $scope.types = settings.music.types;
  $scope.selectedImages = [];
  $scope.fileItems = [];

  musicService.getDoc(docId, function(response) {
   $scope.action.resut = 0;
   $scope.doc = response;
   $scope.initSelectedImages();
  }, function() {
   $scope.action.resut = 1;	 
  });
 };	

 /**
  * 
  */
 $scope.selectImage = function(index) {
  $scope.selectedImages[index] = !$scope.selectedImages[index];
 };
 
 /**
  * 
  */
 $scope.update = function() {
  utils.debug('"Update: ' + JSON.stringify($scope.doc));
  musicService.updateDoc($scope.doc, function() {
   $scope.action.resut = 0;
    utils.debug($scope.doc.id + ' is updated ok');
   }, function() {
	$scope.action.resut = 1;
   });  
 };
 
 /**
  * 
  */
 $scope.removeFile = function(fileItem) {
  utils.debug('remove "' + fileItem.file.name + '"'); 
  var i = $scope.fileItems.indexOf(fileItem);
  if(i != -1) {
   $scope.fileItems.splice(i, 1);
  }
  utils.debug('files :  ' + $scope.fileItems.length);
 };
 
 /**
  * 
  */
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
  
 /**
  * 
  */
 $scope.removePhotos = function() {
  var selectedPhotoIds = []; 
  for (var i=0; i <$scope.selectedImages.length; i++) {
   if ($scope.selectedImages[i]) {
	selectedPhotoIds.push($scope.doc.images[i].id);
   }
  }
  
  if (selectedPhotoIds.length > 0) {
   musicService.removePhotos($scope.doc.id, selectedPhotoIds, function(doc) {
	utils.debug("removePhotos " + selectedPhotoIds + " ok"); 
	$scope.doc = doc;
   }, function() {
	utils.error("removePhotos " + selectedPhotoIds + " ko");
   });
   $scope.initSelectedImages();
  } else {
   utils.debug("nothing to remove");
  }
 };
 
 /**
  * 
  */
 $scope.getGrade = function(value) {
  return refValues.getGradeToString(value);
 }; 
 
 /**
  * 
  */
 $scope.view = function() {
  $location.path('/music_view/' + $scope.doc.id);
 };
 
 /**
  * 
  */
 $scope.remove = function(id) {
  var callback = function() {
   $location.path('/music_list');
  }
  musicService.remove(id, callback, callback);
 };  
 
 /**
  * 
  */
 $scope.navigate = function(nav) {
  $location.path('/music_edit/' + nav.id); 
 };
 
 /**
  * 
  */
 $scope.nextPage = function() {
  appService.nextMusicDocPage(function(nav) {
   $scope.navigate(nav);
  });
 }; 

 /**
  * 
  */
 $scope.previousPage = function() {
  appService.previousMusicDocPage(function(nav) {
   $scope.navigate(nav);
  });
 }; 
 
 /**
  * 
  */
 $scope.first = function() {
  appService.firstMusicDocPage(function(nav) {
   $scope.navigate(nav);
  });
 }; 
 
 /**
  * 
  */
 $scope.last = function() {
  appService.lastMusicDocPage(function(nav) {
   $scope.navigate(nav);
  });
 }; 
 
 /**
  * 
  */
 $scope.next = function() {
  appService.nextMusicDoc(function(nav) {
   $scope.navigate(nav);
  });
 };
 
 /**
  * 
  */
 $scope.previous = function() {
  appService.previousMusicDoc(function(nav) {
   $scope.navigate(nav);
  });
 };
 
 $scope.edit($routeParams.musicDocId);
 
}]);
