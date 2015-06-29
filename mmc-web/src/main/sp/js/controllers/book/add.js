'use strict';

angular.module('mmcApp').controller('bookAddResultCtrl', function($scope, $modalInstance, utils, result) {
 $scope.result = result;
 utils.debug('bookAddResultCtrl doc: ' + JSON.stringify(result));

 /**
  * 
  */
 $scope.addPhotos = function() {
  $modalInstance.close('addPhoto#'+$scope.result.id);
 }; 
 
 /**
  * 
  */
 $scope.newDoc = function() {
  $modalInstance.close('newDoc');
 }; 
});

 /**
  * 
  */
angular.module('mmcApp')
.controller('bookAddCtrl', ['$scope', '$http', '$location', 'musicService','userService', 'utils', 'refValues', '$modal',
function($scope, $http, $location, musicService, userService, utils, refValues, $modal) {

 /**
  * 
  */
 $scope.init = function() {
  $scope.doc = { 'origin' : settings.music.defaultCountry};
  utils.debug('Initial doc: ' + JSON.stringify($scope.doc));  
  $scope.countries = refValues.getCountries();
  $scope.grades = refValues.getGrades();
  $scope.years = refValues.getYears();
  $scope.months = refValues.getMonths();
 };
 
 /**
  * 
  */
 $scope.submitCallback = function(booleanResult, doc) {
  var modalInstance = $modal.open({
   templateUrl : 'result',
   controller : 'bookAddResultCtrl',
   resolve: {
    result: function () {
     return { 'ok' : booleanResult, 'id' : doc.id}; 
    }
   }
  });
  
  modalInstance.result.then(function (mode) {
   utils.debug('Modal closing mode: ' + mode);  
   if (mode == 'newDoc') {
	$scope.init();
   } else if (mode.indexOf('addPhoto',0) == 0){
	 $location.path('/book/edit/' + mode.split('#')[1] + '/photos');  
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
   musicService.addDoc($scope.doc, 'book', function(response) {
   $scope.doc = response; 
   $scope.submitCallback(true, response);
  }, function() {
   $scope.submitCallback(false, null);
  });
 };
 
 /**
  * 
  */
 $scope.uploadImages = function() {
  utils.debug($scope.doc.id);
  var uri = '/book/edit/' + $scope.doc.id;	 
  $location.path(uri); 
 };
 
 $scope.init();
 
}]);