'use strict';

angular.module('mmcApp')
.controller('musicViewCtrl', ['$document', '$scope', '$rootScope', '$http', '$location', '$routeParams','userService', 'musicService', 'refValues', 'utils', 
function($document, $scope, $rootScope, $http, $location, $routeParams, userService, musicService, refValues, utils) {
 
 $scope.action = { 'result' : -1};
 $scope.images = [];
 $scope.currentImagePos = 0;
 $scope.canEdit = userService.userHasRole('WRITE');
 $scope.titleClazz = 'col-md-7';
 
 $scope.setCurrentImage = function(index) {
  if ($scope.doc.images.length == 0) {
   return;
  }
  utils.debug("currentImage: " + index);
  $scope.currentImagePos = index;
  var image = $scope.doc.images[index];
  var size = image.details.m;
  var ratioH = 1;
  var ratioW = 1;
  var height = size.height;
  var width = size.width;
  var heightMax = 400;
  var widthMax = 400;
  var ratio = width / widthMax; 
  height =  height / ratio;
  width =  width / ratio;
  utils.debug('ratio: ' + ratio + ', ' + '"height : ' + size.height + ' => ' + height + ', ' + 'width : ' + size.width + ' => ' + width);
  $scope.currentImage = {
	'm' : {'url' : image.details.m.url, 'height' : height, 'width' : width},
	'o' : {'url' : image.details.o.url}
  };
  utils.debug("currentImage : " + JSON.stringify( $scope.currentImage ));
 };
 
 musicService.getDoc($routeParams.musicDocId, function(response) {
  $scope.action.resut = 0;
  $scope.doc = response;
  if ($scope.doc.images != null && $scope.doc.images.length > 0) {
   utils.debug($scope.images.length + ' photo(s)');
   $scope.setCurrentImage(0);
  } else {
	$scope.titleClazz =  'col-md-12';
  }
   
  $scope.lines = []
  $scope.lines.push(response.artist);
  $scope.lines.push('');
  $scope.lines.push('');
  $scope.lines.push('');
   
  $scope.lines[1] = appendToLine($scope.lines[1], response.issue);
  $scope.lines[1] = appendToLine($scope.lines[1], response.edition, function(value) {
   return 'ed. ' + value;   
  });
  $scope.lines[1] = appendToLine($scope.lines[1], response.origin);
  $scope.lines[1] = appendToLine($scope.lines[1], response.mainType, function(value) {
   if (response.nbType != null && response.nbType > 1) {
    if (response.nbType.indexOf('0') == 0) {
     return response.nbType.substring(1) + ' ' + value; 	 
    }
    return response.nbType + ' ' + value;      
   } else {
    return value;	
   }
  });
  
  $scope.lines[1] = appendToLine($scope.lines[1], response.promo, function(value) {
   return value == true ? 'promo' : '';   
  });
   
  $scope.lines[2] = appendToLine($scope.lines[2], response.recordCompany);
  $scope.lines[2] = appendToLine($scope.lines[2], response.label);
   
  $scope.lines[3] = appendToLine($scope.lines[3], response.serialNumber, function(value) {
   return 'N° ' + value;   
  });
  $scope.lines[3] = appendToLine($scope.lines[3], response.pubNum, function(value) {
   return 'Limited Edition : ' + value + '/' + response.pubTotal;   
  });
  
  for (var j= $scope.lines.length-1; j>=0; j--) {
   if ($scope.lines[j] == '') {
	$scope.lines.splice(j, 1);   
   }  
  }
  utils.debug($scope.lines.length + ' lines: ' + $scope.lines);

 }, function() {
  $scope.action.resut = 1;	 
 });
 
 $scope.gotoEdit = function() {
  $location.path('/music_edit/' + $scope.doc.id);
 };
 
 $scope.gotoFind = function() {
  $location.path('/music_list');
 }; 
}]);
