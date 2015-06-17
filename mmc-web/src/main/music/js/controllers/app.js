'use strict';

angular.module('mmcApp')
.controller('appCtrl', ['$rootScope', '$scope', '$location', 'userService', 'utils', 'refValues', 'appService',
function ($rootScope, $scope, $location, userService, utils, refValues, appService) {
 $rootScope.siteInfos = siteInfos;
 var app = appService.app();
 webUtils.debug('appCtrl : ' + $location.path());	
 utils.verbose = settings.verbose;
 
 refValues.getCountriesPromise().then(function(data){});
 refValues.getGradesPromise().then(function(data){});
 
 $rootScope.$on('showItemsNavBar', function (event, value) {
  $scope.showItemsNavBar = value;	  
 });
 
 if ($location.path() == '/') {
  app.jumbotron = true;
 } else if ($location.path().lastIndexOf('/login', 0) == 0) {
  oauth2.start($location.absUrl(), settings.scopes, false, function(expiresIn) {
   userService.setUserInfosIfAbsent(expiresIn, function(user) {
	appService.setUser(user);
	app.jumbotron = true;
   });
  });
 } else if ($location.path().lastIndexOf('/logout', 0) == 0) {
  userService.logout();
 }
 
 $scope.navIsActive = function (viewLocation) {
  return $location.path().lastIndexOf(viewLocation, 0) == 0; 
 };
 
 $scope.search = function(query) {
  var app = appService.app();
  utils.debug('search : ' + query + ' [universe: ' + app.universe + ']');
  app.query = query;
  // en fonction de l'univers, on redirige sur la page de recherche
  // si aucun univers selectionne par ex, on est sur la home, on fait une recherche sur tous les indexes
  if ('music' == app.universe && $location.path() != '/music_list') {
   $location.path('/music_list');
  } else if ('book' == app.universe && $location.path() != '/book_list') {
   $location.path('/book_list');
  } else if ('misc' == app.universe && $location.path() != '/merchandising_list') {
   $location.path('/merchandising_list');	  
  }
 };
 
 $scope.getPath = function() {
  return '/' + appService.app().universe + '_view'; 
 };

 /**
  * 
  */
 $scope.navigate = function(nav) {
  $location.path('/' + appService.app().universe + '_view/' + nav.id); 
 };
 
 /**
  * 
  */
 $scope.nextPage = function() {
  appService.nextPage(function(nav) {
   $scope.navigate(nav);
  });
 }; 

 /**
  * 
  */
 $scope.previousPage = function() {
  appService.previousPage(function(nav) {
   $scope.navigate(nav);
  });
 }; 
 
 /**
  * 
  */
 $scope.first = function() {
  appService.firstPage(function(nav) {
   $scope.navigate(nav);
  });
 }; 
 
 /**
  * 
  */
 $scope.last = function() {
  appService.lastPage(function(nav) {
   $scope.navigate(nav);
  });
 }; 
 
 /**
  * 
  */
 $scope.next = function() {
  appService.nextDoc(function(nav) {
   $scope.navigate(nav);
  });
 };
 
 /**
  * 
  */
 $scope.previous = function() {
  appService.previousDoc(function(nav) {
   $scope.navigate(nav);
  });
 }; 
 
}]);