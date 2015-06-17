'use strict';

angular.module('mmcApp').controller('bookViewCtrl', 
		['$document', '$scope', '$rootScope', '$http', '$location', '$routeParams'
        ,'userService', 'musicService', 'refValues', 'utils', 'appService', 
function($document, $scope, $rootScope, $http, $location, $routeParams
	   , userService, musicService, refValues, utils, appService) {
 
 /**
  * 
  */
 $scope.view = function(docId) {
  $scope.action = { 'result' : -1};
  $scope.images = [];
  $scope.currentImagePos = 0;
  $scope.canEdit = userService.userHasRole('WRITE');
  $scope.mainRightPartClazz = "col-md-7";
  musicService.getDoc(docId, 'book', function(response) {
   $scope.doc = response;
  
   if ($scope.doc.images != null && $scope.doc.images.length > 0) {
    utils.debug($scope.images.length + ' photo(s)');
    $scope.setCurrentImage(0);
   } else {
	$scope.setCurrentImage(-1);
   }

   $scope.doc.globalRatingTip = refValues.getGradeToString($scope.doc.globalRating);
    
   $scope.infos = []
   for (var i=0; i < 4; i++) {
	$scope.infos.push('');   
   }
   
   $scope.doc.origin = response.origin;
   
   $scope.infos[0] = response.author;
   $scope.infos[1] = appendToLine($scope.infos[1], response.issue);
    
   
   $scope.infos[1] = appendToLine($scope.infos[1], response.reEdition, function(value) {
	return value ? 're-edition' : '';   
   });  
 
   $scope.infos[1] = appendToLine($scope.infos[1], response.promo, function(value) {
    return value == true ? 'promo' : '';   
   });
   
   $scope.infos[2] = appendToLine($scope.infos[2], response.isbn, function(value) {
    return 'ISBN ' + value;   
   });
   
   $scope.infos[2] = appendToLine($scope.infos[2], response.pubNum, function(value) {
    return ' [Limited Edition : ' + value + '/' + response.pubTotal + ']';   
   });

   $scope.infos[3] = appendToLine($scope.infos[3], response.description);
   
   for (var j= $scope.infos.length-1; j>=0; j--) {
    if ($scope.infos[j] == '') {
  	 $scope.infos.splice(j, 1);   
    }  
   }
   utils.debug($scope.infos.length + ' infos: ' + $scope.infos);
   
  }, function() {
	utils.error('getDoc error');
  });
 };
 
 /**
  * 
  */
 $scope.setCurrentImage = function(index) {
  if (index == -1) {
   $scope.currentImage = {
		   'm' : {'url' : 'images/no_view.png', 'height' : 260, 'width' : 400},
		   'o' : {'url' : 'images/no_view.png'}
		  };
  } else {
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
  }
  
  utils.debug("currentImage : " + JSON.stringify( $scope.currentImage ));
 }; 
 
 /**
  * 
  */
 $scope.gotoEdit = function() {
  $location.path('/book_edit/' + $scope.doc.id);
 };
 
 /**
  * 
  */
 $scope.gotoFind = function() {
  $location.path('/book_list');
 }; 
 
 $scope.view($routeParams.bookDocId);

}]);
