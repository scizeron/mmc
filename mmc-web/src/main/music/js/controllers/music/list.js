'use strict';

angular.module('mmcApp')
.controller('musicListCtrl', ['$scope', 'musicService', 'userService', 
function ($scope, musicService, userService) {
 $scope.$emit('jumbotron.show', false);   
 oauth2.start(window.location.href, settings.scopes, false, function(expiresIn) {
	  userService.setUserInfosIfAbsent(expiresIn, function(user) {
	   webUtils.debug('emit "authenticated.user" : ' + user);	  
   $scope.$emit('authenticated.user', user); 
  });
 });
 $scope.doSearch = function (page) {
  $scope.action = { 'result' : -1};	 
  musicService.getDocs(page, true, function(response, selectedPage) {
   $scope.action.result = 0; 
   $scope.docsInfos = [];
   var idxDoc;
   var doc;
   
   for (idxDoc in response.docs) {
	 var doc = response.docs[idxDoc];
	 var docInfos = {'id' : doc.id, 'url' : doc.thumbImageUrl, 'title' : doc.title, 'line1' : '', 'line2' : '', 'line3' : ''};
	 $scope.docsInfos.push(docInfos);
	 
	 docInfos.line1 = appendToLine(docInfos.line1, doc.issue);
	 docInfos.line1 = appendToLine(docInfos.line1, doc.edition, function(value) {
	  return 'ed. ' + value;   
     });
	 docInfos.line1 = appendToLine(docInfos.line1, doc.origin);
	 docInfos.line1 = appendToLine(docInfos.line1, doc.mainType, function(value) {
	  if (doc.nbType != null && doc.nbType > 1) {
       if (doc.nbType.indexOf('0') == 0) {
        return doc.nbType.substring(1) + ' ' + value; 	 
       }
       return doc.nbType + ' ' + value;      
      } else {
       return value;	
      }
     });
  
	 docInfos.line1 = appendToLine(docInfos.line1, doc.promo, function(value) {
	  return 'promo';   
     });
   
	 docInfos.line2 = appendToLine(docInfos.line2, doc.recordCompany);
	 docInfos.line2 = appendToLine(docInfos.line2, doc.label);
   
	 docInfos.line3 = appendToLine(docInfos.line3, doc.serialNumber, function(value) {
	  return 'N° ' + value;   
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
 $scope.doSearch();
}]);
