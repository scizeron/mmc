'use strict';

angular.module('mmcApp')
.controller('musicListCtrl', ['$scope', 'musicService', 'userService', 'refValues', 'appService', 'utils', 
function ($scope, musicService, userService, refValues, appService, utils) {

 $scope.$on('music', function(event, args) {
  $scope.doSearch(0);	 
 });
 
 $scope.selectItem = function(index) {
  appService.nav().currentIndex = index;
 };
	
 $scope.list = function (page) {
  $scope.action = { 'result' : -1};	 
  musicService.getDocs(appService.app().query, page, function(response, selectedPage) {
   $scope.totalPages = response.totalPages;
   $scope.action.result = 0; 
   $scope.docsInfos = [];
   var idxDoc;
   var doc;
   var nav = appService.nav();
   
   nav.type = 'music';
   nav.pageSize = (response.docs == null) ? 0 : response.docs.length;
   nav.totalPages = response.totalPages;
   nav.currentPage = page;
   
   utils.debug('nav: ' + JSON.stringify(nav));
   
   for (idxDoc in response.docs) {
	 var doc = response.docs[idxDoc];
	 var lines = [];
	 var docInfos = {'id' : doc.id, 'url' : doc.thumbImageUrl, 'title' : doc.title
			 , 'line1' : '', 'line2' : '', 'line3' : ''};
	 $scope.docsInfos.push(docInfos);
	 
	 docInfos.sleeveGrade = doc.sleeveGrade;
	 docInfos.recordGrade = doc.recordGrade;
	 docInfos.sleeveGradeTip = refValues.getGradeToString(doc.sleeveGrade);
	 docInfos.recordGradeTip = refValues.getGradeToString(doc.recordGrade);
	 docInfos.origin = (doc.origin == null) ? 'null': doc.origin;
	 
	 docInfos.line1 = doc.artist;
	 docInfos.line2 = appendToLine(docInfos.line2, doc.issue);
	 docInfos.line2 = appendToLine(docInfos.line2, doc.edition, function(value) {
	  return 'ed. ' + value;   
     });
	 docInfos.line2 = appendToLine(docInfos.line2, doc.mainType, function(value) {
      if (doc.nbType != null && doc.nbType > 1) {
       return doc.nbType + ' ' + value;
	  }
      return value;	
     });
  
	 docInfos.line2 = appendToLine(docInfos.line2, doc.promo, function(value) {
	  return value == true ? 'promo' : '';   
     });
   
	 docInfos.line3 = appendToLine(docInfos.line3, doc.recordCompany);
	 docInfos.line3 = appendToLine(docInfos.line3, doc.label);
   	 docInfos.line3 = appendToLine(docInfos.line3, doc.serialNumber, function(value) {
	  return 'N°' + value;   
     });
	 docInfos.line3 = appendToLine(docInfos.line3, doc.pubNum, function(value) {
	  return 'Limited Edition : ' + value + '/' + doc.pubTotal;   
     });
   }
   
   if (response.totalPages < 2) {
    $scope.navigation='';
    
   } else {
	   
    var currentPage = response.page + 1; // premiere page est 0
    var navigHtml = '';
    var i=1;
    var j=0;
    navigHtml = '<ul class="pagination">';
    for (i = 1; i <= response.totalPages; i++) {
     j=i-1; 
     navigHtml+= '<li';
     if (selectedPage == j) {
      navigHtml += ' class="active"';
     }
     navigHtml += '><a href ng-click="list('+j+')">' + i + '</a></li>';
    }
    navigHtml += '</ul>';
    $scope.navigation = navigHtml;	   
   }
  }, function() {
   $scope.action.result = 1;	  
  });
 };

 musicService.clearCachedDoc();
 $scope.list(0);
 
}]);