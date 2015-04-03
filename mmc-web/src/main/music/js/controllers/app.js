'use strict';

angular.module('mmcApp')
.controller('appCtrl', ['$rootScope', '$scope', '$location', 'userService', 'utils', 'refValues', 'appService',
function ($rootScope, $scope, $location, userService, utils, refValues, appService) {
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
  if ('music' == app.universe) {
   if ($location.path() != '/music_list') {
	$location.path('/music_list');
   } else {
	$scope.$broadcast('music');   
   }
  }
 };
 
 $scope.getPath = function() {
	 // regex
  if ($location.path().indexOf('/music_view') == 0) {
	return '/music_view';  
  } else if ($location.path().indexOf('/music_edit') == 0) {
	return '/music_edit';  
  }
 }

 /**
  * 
  */
 $scope.navigate = function(nav) {
  var newPath = $scope.getPath() + '/' + nav.id;
  if (appService.app().selectedTab != null &&  $scope.getPath().indexOf('/music_edit') == 0) {
   newPath = newPath + '/' + appService.app().selectedTab;  
  }
  $location.path(newPath); 
 };
 
 /**
  * 
  */
 $scope.nextPage = function() {
  if ('music' == app.universe) {	 
   appService.nextMusicDocPage(function(nav) {
    $scope.navigate(nav);
   });
  }
 }; 

 /**
  * 
  */
 $scope.previousPage = function() {
  if ('music' == app.universe) {
   appService.previousMusicDocPage(function(nav) {
    $scope.navigate(nav);
   });
  }
 }; 
 
 /**
  * 
  */
 $scope.first = function() {
  if ('music' == app.universe) {
   appService.firstMusicDocPage(function(nav) {
    $scope.navigate(nav);
   });
  }
 }; 
 
 /**
  * 
  */
 $scope.last = function() {
  if ('music' == app.universe) {	 
   appService.lastMusicDocPage(function(nav) {
    $scope.navigate(nav);
   });
  }
 }; 
 
 /**
  * 
  */
 $scope.next = function() {
  if ('music' == app.universe) {	 
   appService.nextMusicDoc(function(nav) {
    $scope.navigate(nav);
   });
  }
 };
 
 /**
  * 
  */
 $scope.previous = function() {
  if ('music' == app.universe) {
   appService.previousMusicDoc(function(nav) {
    $scope.navigate(nav);
   });
  }
 }; 
 
 
}]);