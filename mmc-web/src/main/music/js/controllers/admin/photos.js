'use strict';

angular.module('mmcApp')
.controller('adminPhotosCtrl',['$scope', 'utils', 'photoService', '$timeout', '$modal', function($scope, utils, imgService, $timeout, $modal) {


 $scope.displayPhoto = function (index) {
  utils.debug('display : ' + index);
  var modalInstance = $modal.open({
   templateUrl : 'popupPhoto',
   controller : 'popupPhotoCtrl',
   size: 'lg',
   resolve: {
    datas: function () {
     return {'slides' : $scope.slides, 'index' : index}; 
    }
   }
  });
  modalInstance.result.then(function (event) {
   utils.debug('Receive ' + event + ' event');  
  });
 };
 
 /**
  * 
  */
 $scope.getPhotos = function () {
  $scope.action = {'result' : 0};	
  imgService.getPhotos(null, null, function(response) {
   var count = response.length;
   var imgHtml = '';
   var nbImgPerRow = 11;
   $scope.slides = [];
   $scope.progress = 0;
   $scope.loaded = false;
   $scope.currentIndex = 0;
     
   if (count > 0) {
    for (var x in response) {
     $scope.slides.push({'image': response[x]});	
     utils.debug('- response['+x+']: ' + JSON.stringify(response[x]));
     if (x % nbImgPerRow == 0) {
 	  if (x > 0) {
   	   imgHtml += '</div>';    	   
	  }
	  imgHtml += '<div class="row">'; 
     }
     imgHtml += '<div class="col-sm-1 style="margin: 2px;"><img src="' + response[x].details.t.url + '" ng-click="displayPhoto('+x+')"/></div>';
    }

    if (count % nbImgPerRow != 0) {
	 // completer la derniere ligne en cours
	 for (var col=0; col<(nbImgPerRow-count); col++) {
	  imgHtml += '<div class="col-sm-1"></div>';  
	 }
    }
    
    imgHtml += '</div>';
    $scope.result = imgHtml;   
   } 
  
  }, function() {
   $scope.action.result=1;
   utils.error("getPhotos error");  
  }); //  imgService.getPhotos
 }; // end of $scope.getPhotos
 
 $scope.getPhotos();
 
}]);


angular.module('mmcApp').controller('popupPhotoCtrl', function($scope, $timeout, $modalInstance, utils, datas) {
 $scope.slides = datas.slides;
 //$scope.slide = slides[datas.index];
 $scope.INTERVAL = 3000;
 $scope.pause = false;
 $scope.currentIndex = datas.index;
 
 /**
  * 
  */
 $scope.setCurrentSlideIndex = function (index) {
  $scope.currentIndex = index;
 };

 /**
  * 
  */
 $scope.isCurrentSlideIndex = function (index) {
  return $scope.currentIndex === index;
 };

 /**
  * 
  */
 $scope.prevSlide = function () {
  $scope.currentIndex = ($scope.currentIndex > 0) ? --$scope.currentIndex : $scope.slides.length - 1;
 };

 /**
  * 
  */
 $scope.nextSlide = function (mode) {
  if ( ('auto' == mode && !$scope.pause) || ('manual' == mode) ) {	 
   $scope.currentIndex = ($scope.currentIndex < $scope.slides.length - 1) ? ++$scope.currentIndex : 0;
   if ('auto' == mode && !$scope.pause) {
    $scope.tf = $timeout(function() {$scope.nextSlide('auto')}, $scope.INTERVAL);
   }
  }
 };
 
 /**
  * 
  */
 $scope.delay = function(valueInSec) {
  $scope.INTERVAL = valueInSec * 1000;
 }
 
 /**
  * 
  */
 $scope.doPause = function () {
  utils.debug('pause');
  $scope.pause = true; 
 };
 
 /**
  * 
  */
 $scope.doResume = function () {
  utils.debug('resume');
  $scope.pause = false; 
  $scope.nextSlide('auto');
 };
 /**
  * 
  */
 $scope.close = function() {
  $scope.pause = true;
  if (typeof($scope.tf) != 'undefined') {
   clearTimeout($scope.tf); 
  }
  $modalInstance.close('close');
 }; 
 
 $scope.nextSlide('auto');
 
});


