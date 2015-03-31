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
  $scope.selectedImages = [];
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
 $scope.initUpdatePrices = function() {
  $scope.updatePrices = [];
  // prices
  if ($scope.doc.prices != null) {
	  
   for (var i = 0; i < $scope.doc.prices.length; i++) {
	$scope.updatePrices.push({'value' : $scope.doc.prices[i].price
	                        , 'month' : $scope.doc.prices[i].month
		                    , 'year' : $scope.doc.prices[i].year
		                    , 'display' : 'read'
	                        });
   }    
  }
 };
 
 /**
  * 
  */
 $scope.selectTab = function(tabId) {
  if (typeof($scope.tabs) == 'undefined') {
   $scope.tabs = [];
   $scope.tabs.push({'name':'general','active':true});
   $scope.tabs.push({'name':'purchase','active':false});
   $scope.tabs.push({'name':'photos','active':false});
   $scope.tabs.push({'name':'prices','active':false});   
  }
  
  $scope.tabId = (typeof(tabId) == 'undefined') ? 'general' : tabId; 	

  for (var i = 0 ; i < $scope.tabs.length ; i++) {
   $scope.tabs[i].active = ($scope.tabs[i].name == $scope.tabId) ? true : false;	  
  }
 };

 /**
  * 
  */
 $scope.edit = function(docId, tabId) {
  $scope.action = { 'result' : -1};
  $scope.selectTab(tabId); 
  $scope.countries = refValues.getCountries();
  $scope.grades = refValues.getGrades();
  $scope.nbTypeRange = refValues.getNbTypeRange();
  $scope.years = refValues.getYears();
  $scope.months = refValues.getMonths();
  $scope.defaultMusicCountry = settings.music.defaultCountry;
  $scope.types = settings.music.types;
  $scope.selectedImages = [];
  $scope.fileItems = [];
  $scope.newPrice = {};
  $scope.updatePrice = {};
  $scope.updatePrices = [];
  
  musicService.getDoc(docId, function(response) {
   $scope.action.resut = 0;
   $scope.doc = response;
   $scope.initSelectedImages();
   $scope.initUpdatePrices();
   
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
    $scope.initUpdatePrices();
    $scope.updatePrice = {};
    $scope.newPrice = {};
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
  if (!confirm("Are you sure ?")) {
   return;
  }	 
  var callback = function() {
   $location.path('/music_list');
  }
  musicService.remove(id, callback, callback);
 };  
 
 /**
  * 
  */
 $scope.addNewPrice = function() {
  utils.debug('newPrice:' + JSON.stringify($scope.newPrice));
  $scope.doc.prices.push({'price': $scope.newPrice.value, 'month' : $scope.newPrice.month, 'year' : $scope.newPrice.year});
  $scope.update();
 };
 
 /**
  * 
  */
 $scope.editPrice = function($index) {
  $scope.updatePrices[$index].display = 'edit';
  $scope.updatePrice.value = $scope.updatePrices[$index].value;
  $scope.updatePrice.month = $scope.updatePrices[$index].month;
  $scope.updatePrice.year = $scope.updatePrices[$index].year;
 }
 
 /**
  * 
  */
 $scope.undoEditPrice = function($index) {
  $scope.updatePrices[$index].display = 'read';	 
 }
 
 /**
  * 
  */
 $scope.doUpdatePrice = function($index) {
  $scope.updatePrices[$index].display = 'read';
  $scope.doc.prices[$index].price = $scope.updatePrice.value;
  $scope.doc.prices[$index].month = $scope.updatePrice.month;
  $scope.doc.prices[$index].year = $scope.updatePrice.year;
  utils.debug('updatePrice:' + JSON.stringify($scope.doc.prices[$index]));
  $scope.update();
 }
 
 /**
  * 
  */
 $scope.removePrice = function($index) {
  utils.debug('removePrice:' + JSON.stringify($scope.doc.prices[$index]));
  $scope.doc.prices.splice($index, 1);
  $scope.update();
 }

 $scope.edit($routeParams.musicDocId, $routeParams.tabId);
 
}]);
