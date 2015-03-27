'use strict';
angular.module('mmcApp', [
 'ngRoute'
 ,'ui.bootstrap'
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
  when('/music_list', {
   templateUrl: 'views/music/list.html',
   controller: 'musicListCtrl'
  }).
  when('/music_add', {
   templateUrl: 'views/music/add.html',
   controller: 'musicAddCtrl'
  }).		 
  when('/music_edit/:musicDocId', {
   templateUrl: 'views/music/edit.html',
   controller: 'musicEditCtrl'
  }).
  when('/music_edit/:musicDocId/:tabId', {
   templateUrl: 'views/music/edit.html',
   controller: 'musicEditCtrl'
  }).
  when('/music_view/:musicDocId', {
   templateUrl: 'views/music/view.html',
   controller: 'musicViewCtrl'
  }).
  when('/admin_img', {
   templateUrl: 'views/admin/img.html',
   controller: 'adminImgCtrl',
   role: 'admin'
  }).
  otherwise({
   redirectTo: '/home',
   role: 'anonymous',
  });
})
.config(['$httpProvider', function($httpProvider) {  
 $httpProvider.interceptors.push('oauth2Interceptor');
}])
.run(function($rootScope, $location, userService, utils, appService) {
 
 appService.init();
 
 $rootScope.$on( "$routeChangeStart", function(event, next, current) { 
  var app = appService.app();
  
  utils.debug('********************************** NEXT **********************************');
  utils.debug('- app  : ' + JSON.stringify(app));
  utils.debug('- nav  : ' + JSON.stringify(appService.nav()));
  utils.debug('- next : ' + JSON.stringify(next));
  utils.debug('**************************************************************************');
  
  var nextPath = (typeof(next.$$route) != 'undefined') ? next.$$route.originalPath : '/home';
  
  if (nextPath == '/logout') {
   appService.setUser(null);
   app.universe = null;
   app.jumbotron = true;	  
   return;
  }
  
  var expectedRole = (typeof(next.$$route) != 'undefined' && typeof(next.$$route.role) != 'undefined') ? next.$$route.role : 'user';
  var universe = 'music';
  var jumbotron = false;

  if (nextPath == '' || nextPath == '/' || nextPath == '/home') {
   expectedRole = 'anonymous';
   jumbotron = true;
  }
  
  appService.setUser(userService.getUser());
  app.jumbotron = jumbotron;
  app.universe = universe;
  
  utils.debug('expectedRole: ' + expectedRole + ' for "' + nextPath + '"');
  
  if (expectedRole == 'anonymous') {
   utils.debug('anonymous access to "' + nextPath + '"');
   return;
  }
  
  if (!userService.loggedInUser()) {
   utils.debug('Need to sign-in before accessing to "' + nextPath + '"');
   $location.path('/login');
  
  } else {

   if (expectedRole == 'admin' && !userService.loggedInAdminUser()) {
	utils.debug('Access denied, need to be ADMIN to access to "' + nextPath + '"');
	$location.path('/home');
   } else {
	utils.debug('Go to "' + nextPath + '"');
   }
  } 
 })
});