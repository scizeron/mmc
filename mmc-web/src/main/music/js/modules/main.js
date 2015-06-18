'use strict';
angular.module('mmcApp', [
  'ngRoute'
 ,'ui.bootstrap'
 ,'pascalprecht.translate'
 ,'n3-line-chart' 
])
.config(function($routeProvider) {
 $routeProvider.
  when('/', {
   templateUrl: 'views/home.html',
   controller: 'appCtrl',
   role: 'anonymous',
  }).
  when('/home', {
   templateUrl: 'views/home.html',
   controller: 'appCtrl',
   role: 'anonymous'
  }).
  when('/login', {
   templateUrl: 'views/home.html',
   controller: 'appCtrl',
   role: 'anonymous'
  }).
  when('/logout', {
   templateUrl: 'views/home.html',
   controller: 'appCtrl',
  }).  
  when('/music/add', {
   templateUrl: 'views/music/add.html',
   controller: 'musicAddCtrl'
  }).		 
  when('/music/find', {
   templateUrl: 'views/music/find.html',
   controller: 'musicListCtrl'
  }).
  when('/music/view/:musicDocId', {
   templateUrl: 'views/music/view.html',
   controller: 'musicViewCtrl'
  }).
  when('/music/edit/:musicDocId', {
   templateUrl: 'views/music/edit.html',
   controller: 'musicEditCtrl',
   role: 'write'
  }).
  when('/music/edit/:musicDocId/:tabId', {
   templateUrl: 'views/music/edit.html',
   controller: 'musicEditCtrl',
   role: 'write'
  }).
  when('/book/add', {
   templateUrl: 'views/book/add.html',
   controller: 'bookAddCtrl'
  }).  
  when('/book/find', {
   templateUrl: 'views/book/find.html',
   controller: 'bookListCtrl'
  }).  
  when('/book/view/:bookDocId', {
   templateUrl: 'views/book/view.html',
   controller: 'bookViewCtrl'
  }).  
  when('/book/edit/:bookDocId', {
   templateUrl: 'views/book/edit.html',
   controller: 'bookEditCtrl',
   role: 'write'
  }).
  when('/book/edit/:bookDocId/:tabId', {
   templateUrl: 'views/book/edit.html',
   controller: 'bookEditCtrl',
   role: 'write'
  }).  
  when('/admin/music/photos', {
   templateUrl: 'views/admin/music/photos.html',
   controller: 'adminMusicPhotosCtrl',
   role: 'admin'
  }).
  when('/admin/book/photos', {
   templateUrl: 'views/admin/book/photos.html',
   controller: 'adminBookPhotosCtrl',
   role: 'admin'
  }).	  
  when('/admin/music/list', {
   templateUrl: 'views/admin/music/list.html',
   controller: 'musicListingCtrl',
   role: 'admin'
  }).  
  when('/admin/book/list', {
   templateUrl: 'views/admin/book/list.html',
   controller: 'bookListingCtrl',
   role: 'admin'
  }).  
  otherwise({
   redirectTo: '/home',
   role: 'anonymous',
  });
})
.config(['$httpProvider', '$translateProvider', function($httpProvider, $translateProvider) {  
 $httpProvider.interceptors.push('oauth2Interceptor');
 $translateProvider.preferredLanguage('en');
}])
.run(function($rootScope, $location, userService, utils, appService) {
 
 appService.init();
 
 $rootScope.$on( "$routeChangeStart", function(event, next, current) { 
  var app = appService.app();
  var nextPath = (typeof(next.$$route) != 'undefined') ? next.$$route.originalPath : '/home';
  
  if (nextPath == '/logout') {
   appService.setUser(null);
   app.universe = null;
   app.jumbotron = true;	  
   return;
  }
  
  appService.setUser(userService.getUser());
  
  if (nextPath == '/music/find') {
   app.universe = 'music';  
  } else if (nextPath == '/book/find') {
   app.universe = 'book';	  
  } else if (nextPath == '/misc/find') {
   app.universe = 'misc';	  
  }

  if (nextPath == '' || nextPath == '/' || nextPath == '/home') {
   expectedRole = 'anonymous';
   app.jumbotron = true;
  } else {
   app.jumbotron = false;	  
  }

  utils.debug('********************************** NEXT **********************************');
  utils.debug('- app              : ' + JSON.stringify(app));
  utils.debug('- nav              : ' + JSON.stringify(appService.nav()));
  utils.debug('- next             : ' + JSON.stringify(next));
  utils.debug('- $location.path() : ' + $location.path());
  utils.debug('**************************************************************************');

  
  var expectedRole = (typeof(next.$$route) != 'undefined' && typeof(next.$$route.role) != 'undefined') ? next.$$route.role : 'read';
  utils.debug('- expected role    : "' + expectedRole + '" for "' + nextPath + '"');
  
  if (expectedRole == 'anonymous') {
   utils.debug('anonymous access to "' + nextPath + '"');
   return;
  }
  
  if (!userService.loggedInUser()) {
   utils.debug('Need to sign-in before accessing to "' + nextPath + '"');
   $location.path('/login');
  
  } else {
   if (!userService.userHasRole(expectedRole)) {
	utils.debug('***** Access denied, insuffisant role to access to "' + nextPath + '" *****');
	$location.path('/home');
   } else {
	utils.debug('Go to "' + nextPath + '"');
   }
  } 
  
  if (nextPath.indexOf('/view/') > 0) {
   $rootScope.$broadcast('showItemsNavBar', true);  
  } else {
   $rootScope.$broadcast('showItemsNavBar', false);
  }
  
 })
});