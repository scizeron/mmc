'use strict';

angular.module('mmcApp').controller('musicEditResultCtrl', function($scope, $modalInstance, utils, result) {
 $scope.result = result;
 utils.debug('musicEditResultCtrl doc: ' + JSON.stringify(result));

 /**
  * 
  */
 $scope.closePopup = function(event) {
  if ('back' == event) {
   $modalInstance.close('back');

  } else if ('yesdelete' == event) {
   $modalInstance.close('performDelete');  
  
  } else if ('nodelete' == event) {
	utils.debug('delete cancelled'); 
  }
  
  $modalInstance.close('close');
 };
 
});

angular.module('mmcApp')
.controller('musicEditCtrl', ['$document', '$scope', '$rootScope', '$http', '$location', '$routeParams'
                               ,'userService', 'musicService', 'refValues', 'utils'
                               , 'appService', '$modal', 
function($document, $scope, $rootScope, $http, $location, $routeParams
		, userService, musicService, refValues, utils, appService, $modal) {
 
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
 $scope.displayPopup = function(booleanResult, docId, action) {
  var modalInstance = $modal.open({
   templateUrl : 'result',
   controller : 'musicEditResultCtrl',
   resolve: {
    result: function () {
 	 return {'ok' : booleanResult, 'id' : docId, 'action' : action};  
    }
   }
  });
  
  modalInstance.result.then(function (event) {
   if ('performDelete' == event) {
	var callback = function() {
	 $location.path('/music_list');
	};
	musicService.remove($scope.doc.id, callback, callback);	   
   } else if ('back' == event) {
    $location.path('/music_view/' + $scope.result.id);  
   }
   utils.debug(event);	  
  });
 };
 
 /**
  * 
  */
 $scope.initUpdatePrices = function() {
  $scope.updatePrices = [];
  $scope.data = [];
  // prices
  if ($scope.doc.prices != null) {
   // on rajoute le prix d'achat
   if ($scope.doc.purchaseMonth != null 
	&& $scope.doc.purchaseYear != null
	&& $scope.doc.purchasePrice != null) {
    $scope.data.push({ x: new Date($scope.doc.purchaseYear, $scope.doc.purchaseMonth,1,0,0,0,0)
    , price: $scope.doc.purchasePrice});	   
   }
  
   for (var i = 0; i < $scope.doc.prices.length; i++) {
	$scope.updatePrices.push({'value' : $scope.doc.prices[i].price
	                        , 'month' : $scope.doc.prices[i].month
		                    , 'year' : $scope.doc.prices[i].year
		                    , 'display' : 'read'
		                    , 'type' : 'update' 
	                        });
	$scope.data.push({ x: new Date($scope.updatePrices[i].year, $scope.updatePrices[i].month,1,0,0,0,0)
		, price: $scope.updatePrices[i].value});	
   }  

   // tri DESC pour tableau
   $scope.updatePrices.sort(function(p1,p2) {
	return (p2.year  - p1.year == 0) ? p2.month - p1.month : p2.year  - p1.year;
   });   

   // tri ASC pour abscisse
   $scope.data.sort(function(p1,p2) {
    return p1.x - p2.x;
   });

  }
  
  $scope.options = {
   lineMode: "basis",
   tension: 1,
   axes: {x: {type: "date", key: "x"}, y: {type: "linear"}},
   tooltipMode: "dots",
   drawLegend: true,
   drawDots: true,
   stacks: [],
   series: [
    { y: "price",
      label: "Prices",
      color: "#9467bd",
      axis: "y",
      type: "line",
      thickness: "1px",
      visible: true,
      dotSize: 2,
      id: "prices",
      drawDots: true}
   ],
   tooltip: {
	mode: "scrubber",
	formatter: function (x, y, series) {
	return moment(x).fromNow() + ' : ' + y + ' €';
	}
   },
   columnsHGap: 5
  };
  
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

  var previousSelected = appService.app().selectedTab;
  utils.debug('tabId: ' + tabId + ', previous: ' + previousSelected);
  $scope.tabId = (typeof(tabId) != 'undefined' && tabId != null) ? tabId : (previousSelected != null) ? previousSelected : 'general'; 
  //appService.app().selectedTab = $scope.tabId;
  /**
   * problme quand on retourne sur Find, selectTab est invoque ???
   */
  
  utils.debug('Selected tab : ' + $scope.tabId);

  for (var i = 0 ; i < $scope.tabs.length ; i++) {
   $scope.tabs[i].active = ($scope.tabs[i].name == $scope.tabId) ? true : false;	  
  }
 };

 /**
  * 
  */
 $scope.edit = function(docId, tabId) {
  utils.debug('Init edit : set the selected tab with ' + tabId);
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
  $scope.colors = refValues.getColors(); 
  musicService.getDoc(docId, function(response) {
   $scope.doc = response;
   $scope.initSelectedImages();
   $scope.initUpdatePrices();
   $scope.matrices = [];
   if ($scope.doc.sideMatrixes != null) {
    for (var i=0; i < $scope.doc.sideMatrixes.length; i++) {
     $scope.matrices.push({'disc': $scope.doc.sideMatrixes[i].disc, 'side': $scope.doc.sideMatrixes[i].side, 'value' : $scope.doc.sideMatrixes[i].value});  
	}   
   }
  }, function() {
	$scope.displayPopup(false, docId, 'edit');	 
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
  // matrices
  $scope.doc.sideMatrixes = $scope.matrices;
  musicService.updateDoc($scope.doc, function(response) {
	$scope.displayPopup(true, $scope.doc.id, 'save');
	$scope.doc = response;
	$scope.initUpdatePrices();
    $scope.updatePrice = {};
    $scope.newPrice = {};
   }, function() {
	$scope.displayPopup(false, $scope.doc.id, 'save');	 
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
  $scope.displayPopup(false, $scope.doc.id, 'delete');
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
 $scope.editPrice = function(index) {
  $scope.updatePrices[index].display = 'edit';
  $scope.updatePrice.value = $scope.updatePrices[index].value;
  $scope.updatePrice.month = $scope.updatePrices[index].month;
  $scope.updatePrice.year = $scope.updatePrices[index].year;
 };
 
 /**
  * 
  */
 $scope.undoEditPrice = function(index) {
  $scope.updatePrices[index].display = 'read';	 
 };
 
 /**
  * 
  */
 $scope.doUpdatePrice = function(index) {
  $scope.updatePrices[index].display = 'read';
  $scope.doc.prices[index].price = $scope.updatePrice.value;
  $scope.doc.prices[index].month = $scope.updatePrice.month;
  $scope.doc.prices[index].year = $scope.updatePrice.year;
  utils.debug('updatePrice:' + JSON.stringify($scope.doc.prices[index]));
  $scope.update();
 };
 
 /**
  * 
  */
 $scope.removePrice = function(index) {
  utils.debug('removePrice:' + JSON.stringify($scope.doc.prices[index]));
  $scope.doc.prices.splice(index, 1);
  $scope.update();
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

 $scope.edit($routeParams.musicDocId, $routeParams.tabId);
 
}]);
