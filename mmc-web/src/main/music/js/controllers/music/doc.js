'use strict';

angular.module('mmcApp')
.controller('musicEditViewCtrl', ['$document', '$scope', '$rootScope', '$http', '$location', '$routeParams','userService', 'musicService', 'refValues', 'utils', 
function($document, $scope, $rootScope, $http, $location, $routeParams, userService, musicService, refValues, utils) {
 
 $scope.action = { 'result' : -1};
 $scope.images = [];
 $scope.currentImage = null;
 $scope.currentImagePos = 0;
 
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
 } else {
  $scope.setCurrentImage = function(index) {
   $scope.currentImagePos = index;
   $scope.currentImage = {'url' : $scope.images[index].details.m.url};
  };
  $scope.onImage = function(eventType) {
	utils.debug('onImage : ' + eventType + ', currentPos:' + $scope.currentImagePos);
	var image = $scope.images[$scope.currentImagePos];
	if ('leave' == eventType) {
	 $scope.currentImage = {'url' : image.details.m.url};	
	} else if ('over' == eventType) {
	 $scope.currentImage = {'url' : image.details.o.url};
	}
  };
 }
 
 musicService.getDoc($routeParams.musicDocId, function(response) {
  $scope.action.resut = 0;
  $scope.canEdit = userService.userHasRole('WRITE');
  $scope.doc = response;

  if ($location.path().indexOf('/music_view/') > -1) {
   if (response.images != null && response.images.length > 0) {
    $scope.images = response.images;
    $scope.currentImagePos = 0
    $scope.currentImage = {
     'url' : $scope.images[0].details.m.url
    };
    
    $scope.slider="<script type=\"text/javascript\">$(document).ready(function(){$(\"#my-als-list\").als();});</script>";
   }
   
   $scope.lines = []
   var line1 = response.artist;
   var line2 = '';
   var line3 = '';
   var line4 = '';
   
   line2 = appendToLine(line2, response.issue);
   line2 = appendToLine(line2, response.edition, function(value) {
	return 'ed. ' + value;   
   });
   line2 = appendToLine(line2, response.origin);
   line2 = appendToLine(line2, response.mainType, function(value) {
    if (response.nbType != null && response.nbType > 1) {
     if (response.nbType.indexOf('0') == 0) {
      return response.nbType.substring(1) + ' ' + value; 	 
     }
     return response.nbType + ' ' + value;      
    } else {
     return value;	
    }
   });
  
   line2 = appendToLine(line2, response.promo, function(value) {
	return 'promo';   
   });
   
   line3 = appendToLine(line3, response.recordCompany);
   line3 = appendToLine(line3, response.label);
   
   line4 = appendToLine(line4, response.serialNumber, function(value) {
	return 'N° ' + value;   
   });
   line4 = appendToLine(line4, response.pubNum, function(value) {
	return 'Limited Edition : ' + value + '/' + response.pubTotal;   
   });
   
   $scope.lines.push(line1);
   $scope.lines.push(line2);
   $scope.lines.push(line3);
   $scope.lines.push(line4);
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
 
 $scope.remove = function(id) {
  var callback = function() {
   $location.path('/music_list');
  }
  musicService.remove(id, callback, callback);
 };  
 
}]);
