'use strict';

angular.module('mmcApp')
.controller('adminPhotosCtrl',['$scope', 'utils', 'photoService', function($scope, utils, imgService) {
 $scope.imgPerPage = 16;
 $scope.nbImgPerRow = 4;
 $scope.getPhotos = function (page) {
  $scope.action = {'result' : 0};	
  imgService.getPhotos(page, $scope.imgPerPage, function(response) {
   var imgPerPage = 16;
   var nbImgPerRow = 4;	  
   var count = response.length;
   utils.debug('---------------------------------------');
   utils.debug(' page: ' + page + ', photos: ' + count);
   utils.debug('---------------------------------------');
   var imgHtml = '';
   var navigHtml = '';
   var pages;
   if (count > 0) {
    for (var x in response) {
     utils.debug('photo['+x+']:  ' +JSON.stringify(response[x])); 
     if (x % nbImgPerRow == 0) {
 	  if (x > 0) {
   	   imgHtml += '</div>';    	   
	  }
	  imgHtml += '<div class="row">'; 
     }
     imgHtml += '<div class="col-sm-3 style="margin-bottom: 5px;"><img src="' + response[x].details.t.url + '"/></div>';
    } // end of for (var x in response) 
   
    if (count % nbImgPerRow != 0) {
	 // completer la derniere ligne en cours
	 for (var col=0; col<(nbImgPerRow-count); col++) {
	  imgHtml += '<div class="col-sm-3"></div>';  
	 }
    }
    
    imgHtml += '</div>';
    
    // navigation
    if (count < imgPerPage) {
     // pas d'image dans la page suivante
     pages =  imgService.getPageCount();
    } else {
     // potentiellement des images dans la page suivante	
     pages = imgService.incrPageCount(page);
    }

    utils.debug('navigation:' + pages);
    
    navigHtml = '<div><ul class="pagination">';
    for (var p = 1; p <= pages + 1; p++) {
     navigHtml+= '<li';
     if (p == page) {
      navigHtml += ' class="active"';  
     }
     navigHtml += '><a href ng-click="getPhotos('+p+')">' + p + '</a></li>';
    } 
    navigHtml += '</ul></div>';
    $scope.result = navigHtml + imgHtml;   
   
   } else { // if (count > 0) 
    // pas de photos pour cette page
    imgService.decrPageCount(); 
   } 
  }, function() {
   $scope.action.result=1;
   utils.error("getPhotos error");  
  }); //  imgService.getPhotos
 }; // end of $scope.getPhotos
 $scope.getPhotos(1);
}]);