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
  when('/music_add', {
   templateUrl: 'views/music/add.html',
   controller: 'musicAddCtrl'
  }).		 
  when('/music_list', {
   templateUrl: 'views/music/list.html',
   controller: 'musicListCtrl'
  }).
  when('/music_view/:musicDocId', {
   templateUrl: 'views/music/view.html',
   controller: 'musicViewCtrl'
  }).
  when('/music_edit/:musicDocId', {
   templateUrl: 'views/music/edit.html',
   controller: 'musicEditCtrl',
   role: 'write'
  }).
  when('/music_edit/:musicDocId/:tabId', {
   templateUrl: 'views/music/edit.html',
   controller: 'musicEditCtrl',
   role: 'write'
  }).
  when('/music_listing', {
   templateUrl: 'views/admin/music/list.html',
   controller: 'musicListingCtrl',
   role: 'admin'
  }).  
  when('/music_admin_photos', {
   templateUrl: 'views/admin/photos.html',
   controller: 'adminPhotosCtrl',
   role: 'admin'
  }).
  when('/book_add', {
   templateUrl: 'views/book/add.html',
   controller: 'bookAddCtrl'
  }).  
  when('/book_list', {
   templateUrl: 'views/book/list.html',
   controller: 'bookListCtrl'
  }).  
  when('/book_view/:bookDocId', {
   templateUrl: 'views/book/view.html',
   controller: 'bookViewCtrl'
  }).  
  when('/book_edit/:musicDocId', {
   templateUrl: 'views/book/edit.html',
   controller: 'bookEditCtrl',
   role: 'write'
  }).
  when('/book_edit/:musicDocId/:tabId', {
   templateUrl: 'views/book/edit.html',
   controller: 'bookEditCtrl',
   role: 'write'
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
  
  if (nextPath == '/music_list') {
   app.universe = 'music';  
  } else if (nextPath == '/book_list') {
   app.universe = 'book';	  
  } else if (nextPath == '/merchandising_list') {
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
  
  if (nextPath.indexOf('_view/') > 0) {
   $rootScope.$broadcast('showItemsNavBar', true);  
  } else {
   $rootScope.$broadcast('showItemsNavBar', false);
  }
  
 })
});