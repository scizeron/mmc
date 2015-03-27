'use strict';

angular.module('mmcApp').controller('musicAddResultCtrl', function($scope, $modalInstance, utils, result) {
 $scope.doc = result.doc;
 $scope.ok = false;
 if ($scope.doc != null) {
  $scope.ok = true;
  utils.debug('musicAddResultCtrl doc: ' + $scope.doc.id + ', result: ' + $scope.ok);
 } else {
  utils.debug('musicAddResultCtrl, result: ' + $scope.ok); 
 }
 
 $scope.newWithValues = function() {
  $modalInstance.close('newWithValues');
 }; 
 
 $scope.newDoc = function() {
  $modalInstance.close('reset');
 }; 
});

angular.module('mmcApp')
.controller('musicAddCtrl', ['$scope', '$http', '$location', 'musicService','userService', 'utils', 'refValues', '$modal',
function($scope, $http, $location, musicService, userService, utils, refValues, $modal) {

 /**
  * 
  */
 $scope.init = function() {
  $scope.doc = { 'mainType' : 'LP', 'origin' : settings.music.defaultCountry, 'obiPos':'V'};
  utils.debug('Initial doc: ' + JSON.stringify($scope.doc));  
  
  $scope.action = { 'result' : -1};
  
  $scope.countries = refValues.getCountries();
  $scope.grades = refValues.getGrades();
  $scope.nbTypeRange = refValues.getNbTypeRange();
  $scope.years = refValues.getYears();
  $scope.types = settings.music.types;
 };
 
 /**
  * 
  */
 $scope.submitCallback = function(response) {
  $scope.doc = response;
  var modalInstance = $modal.open({
   templateUrl : 'result',
   controller : 'musicAddResultCtrl',
   resolve: {
    result: function () {
     var result = { 'doc' : $scope.doc}; 
     utils.debug('result: ' + JSON.stringify(result));
	 return result;  
    }
   }
  });
  
  modalInstance.result.then(function (mode) {
   utils.debug('Modal closing mode: ' + mode);  
   if ('reset') {
    $scope.cancel();
   }
  }, function () {
   utils.debug('Modal dismissed at: ' + new Date());
  });
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
 $scope.submit = function() {
  musicService.addDoc($scope.doc, function(response) {
   $scope.doc = response; 
   $scope.submitCallback(response);
  }, function() {
   $scope.submitCallback(null);
  });
 };
  
 /**
  * 
  */
 $scope.cancel = function() {
  $scope.doc = {};
 };
 
 /**
  * 
  */
 $scope.uploadImages = function() {
  utils.debug($scope.doc.id);
  var uri = '/music_edit/' + $scope.doc.id;	 
  $location.path(uri); 
 };
 
 $scope.init();
 
}]);