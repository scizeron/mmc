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
   $scope.searchResult = response;
   $scope.pageSelectedPage = selectedPage;
   if ($scope.searchResult == null || $scope.searchResult.totalPages < 2) {
    $scope.navigation='';   
   } else {
    var currentPage = $scope.searchResult.page + 1; // premiere page est 0
    var navigHtml = '';
    var i=1;
    var j=0;
    navigHtml = '<ul class="pagination">';
    for (i = 1; i <= $scope.searchResult.totalPages; i++) {
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
