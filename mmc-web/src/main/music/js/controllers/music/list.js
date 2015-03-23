'use strict';

angular.module('mmcApp')
.controller('musicListCtrl', ['$scope', 'musicService', 'userService', 'refValues',
function ($scope, musicService, userService, refValues) {
 musicService.clearCachedDoc();
 
 $scope.doSearch = function (page) {
  $scope.action = { 'result' : -1};	 
  musicService.getDocs(page, true, function(response, selectedPage) {
   $scope.action.result = 0; 
   $scope.docsInfos = [];
   var idxDoc;
   var doc;
   
   for (idxDoc in response.docs) {
	 var doc = response.docs[idxDoc];
	 var lines = [];
	 var docInfos = {'id' : doc.id, 'url' : doc.thumbImageUrl, 'title' : doc.title
			 , 'line1' : '', 'line2' : '', 'line3' : ''};
	 $scope.docsInfos.push(docInfos);
	 
	 docInfos.sleeveGrade = doc.sleeveGrade;
	 docInfos.recordGrade = doc.recordGrade;
	 
	 if (doc.sleeveGrade != null) {
	  var grade = refValues.getGrade(doc.sleeveGrade);
	  docInfos.sleeveGradeTip = grade.code + ' - ' + grade.name;
	 }
	 
	 if (doc.recordGrade != null) {
	  var grade = refValues.getGrade(doc.recordGrade);
	  docInfos.recordGradeTip = grade.code + ' - ' + grade.name;
	 }
	 
	 docInfos.line1 = doc.artist;
	 docInfos.line2 = appendToLine(docInfos.line2, doc.issue);
	 docInfos.line2 = appendToLine(docInfos.line2, doc.edition, function(value) {
	  return 'ed. ' + value;   
     });
	 docInfos.line2 = appendToLine(docInfos.line2, doc.origin);
	 docInfos.line2 = appendToLine(docInfos.line2, doc.mainType, function(value) {
	  if (doc.nbType != null && doc.nbType > 1) {
       if (doc.nbType.indexOf('0') == 0) {
        return doc.nbType.substring(1) + ' ' + value; 	 
       }
       return doc.nbType + ' ' + value;      
      } else {
       return value;	
      }
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
   
   $scope.pageSelectedPage = selectedPage;
   if (response == null || response.totalPages < 2) {
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
     navigHtml += '><a href ng-click="doSearch('+j+')">' + i + '</a></li>';
    }
    navigHtml += '</ul>';
    $scope.navigation = navigHtml;	   
   }
  }, function() {
   $scope.action.result = 1;	  
  });
 };
 $scope.doSearch(0);
}]);
