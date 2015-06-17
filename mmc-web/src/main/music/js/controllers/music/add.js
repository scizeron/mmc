'use strict';

angular.module('mmcApp').controller('musicAddResultCtrl', function($scope, $modalInstance, utils, result) {
 $scope.result = result;
 utils.debug('musicAddResultCtrl doc: ' + JSON.stringify(result));

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
.controller('musicAddCtrl', ['$scope', '$http', '$location', 'musicService','userService', 'utils', 'refValues', '$modal',
function($scope, $http, $location, musicService, userService, utils, refValues, $modal) {

 /**
  * 
  */
 $scope.init = function() {
  $scope.doc = { 'mainType' : 'LP', 'nbType' : 1, 'origin' : settings.music.defaultCountry, 'obiPos':'V', 'vinylColor' : '#000000'};
  utils.debug('Initial doc: ' + JSON.stringify($scope.doc));  
  $scope.newMatrice = {'disc':'', 'side' : 'A', 'value':''};
  $scope.countries = refValues.getCountries();
  $scope.grades = refValues.getGrades();
  $scope.nbTypeRange = refValues.getNbTypeRange();
  $scope.years = refValues.getYears();
  $scope.months = refValues.getMonths();
  $scope.types = settings.music.types;
  $scope.colors = refValues.getColors();
  $scope.matrices = [];
 };
 
 /**
  * 
  */
 $scope.submitCallback = function(booleanResult, doc) {
  var modalInstance = $modal.open({
   templateUrl : 'result',
   controller : 'musicAddResultCtrl',
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
	 $location.path('/music_edit/' + mode.split('#')[1] + '/photos');  
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
  // ajout matrices
  if ($scope.matrices.length > 0) {
   $scope.doc.sideMatrixes = [];
   for (var i=0; i < $scope.matrices.length; i++) {
	$scope.doc.sideMatrixes.push({'disc': $scope.matrices[i].disc, 'side': $scope.matrices[i].side, 'value' : $scope.matrices[i].value});  
   }
  }
	 
  musicService.addDoc($scope.doc, 'music', function(response) {
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
  var uri = '/music_edit/' + $scope.doc.id;	 
  $location.path(uri); 
 };
 
 /**
  * 
  */
 $scope.addNewMatrice = function() {
  if ($scope.newMatrice.value != '') {
   utils.debug('Add matrice : ' + JSON.stringify($scope.newMatrice));
   $scope.matrices.push($scope.newMatrice);
   $scope.newMatrice = {'disc':'', 'side' : 'A', 'value':''};
  }
 };
 
 /**
  * 
  */
 $scope.removeMatrice = function(index) {
  utils.debug('removeMatrice:' + JSON.stringify($scope.matrices[index]));
  $scope.matrices.splice(index, 1);
 };
 
 $scope.init();
 
}]);