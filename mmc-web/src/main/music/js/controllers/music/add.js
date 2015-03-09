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
 oauth2.start(window.location.href, settings.scopes, false, function(expiresIn) {
  userService.setUserInfosIfAbsent(expiresIn, function(user) {
  utils.debug('emit "authenticated.user" : ' + user);	  
   $scope.$emit('authenticated.user', user); 
  });
 });
 
 $scope.$emit('jumbotron.show', false);
  
 $scope.doc = {};
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
 
 $scope.action = { 'result' : -1};
 
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
 
 $scope.submit = function() {
  musicService.addDoc($scope.doc, function(response) {
   $scope.doc = response; 
   $scope.submitCallback(response);
  }, function() {
   $scope.submitCallback(null);
  });
 };
  
 $scope.cancel = function() {
  $scope.doc = {};
 };
 
 $scope.uploadImages = function() {
  utils.debug($scope.doc.id);
  var uri = '/music_edit/' + $scope.doc.id;	 
  $location.path(uri); 
 };
 
}]);