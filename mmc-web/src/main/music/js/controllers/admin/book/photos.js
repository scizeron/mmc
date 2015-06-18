'use strict';

angular.module('mmcApp')
.controller('adminBookPhotosCtrl',['$scope', 'utils', 'photoService', '$timeout', '$modal', function($scope, utils, imgService, $timeout, $modal) {


 $scope.displayPhoto = function (index) {
  utils.debug('display : ' + index);
  var modalInstance = $modal.open({
   templateUrl : 'popupPhoto',
   controller : 'popupBookPhotoCtrl',
   windowClass: 'app-modal-window',
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
  imgService.getPhotos(null, null, 'book', function(response) {
   var count = response.length;
   var nbImgPerRow = 11;
   $scope.slides = [];
   var imgHtml = '';     
   
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


angular.module('mmcApp').controller('popupBookPhotoCtrl', function($scope, $location, $timeout, $modalInstance, utils, musicService, refValues, datas) {
 $scope.DELAY = 5;
 $scope.slides = datas.slides;
 $scope.pause = false;
 $scope.currentIndex = datas.index - 1;
 $scope.doc = null;
 
 /**
  * 
  */
 $scope.loadCurrentInfos = function(index) {
  var docId = $scope.slides[index].image.docId;
  if (typeof(docId) != 'undefined' && docId != null) {
   musicService.getDoc(docId, 'book', function(response) {
	$scope.doc = response;
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
   }, function() {
	$scope.doc = null;
   });
  } else {
   $scope.doc = null;	  
  }
 };
 
 /**
  * 
  */
 $scope.setCurrentSlideIndex = function (index) {
  $scope.currentIndex = index;
  $scope.loadCurrentInfos($scope.currentIndex);
 };

 /**
  * 
  */
 $scope.isCurrentSlideIndex = function (index) {
  return $scope.currentIndex == index;
 };

 /**
  * 
  */
 $scope.prevSlide = function () {
  $scope.setCurrentSlideIndex(($scope.currentIndex > 0) ? --$scope.currentIndex : $scope.slides.length - 1);
 };

 /**
  * 
  */
 $scope.nextSlide = function (mode) {
  if ( ('auto' == mode && !$scope.pause) || ('manual' == mode) ) {	 
   $scope.setCurrentSlideIndex(($scope.currentIndex < $scope.slides.length - 1) ? ++$scope.currentIndex : 0);
   if ('auto' == mode && !$scope.pause) {
	utils.debug('Wait ' + $scope.DELAY + ' s ...');
    $scope.tf = $timeout(function() {$scope.nextSlide('auto')}, $scope.DELAY * 1000);
   }
  }
 };
 

 
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
 
 /**
  * 
  */
 $scope.viewCurrent = function() {
  var docId = $scope.doc != null ? $scope.doc.id : null;
  if (docId != null) {
   $scope.close();
   $location.path('/book/view/' + docId);
  }
 };
 
 $scope.nextSlide('auto');
 
});


