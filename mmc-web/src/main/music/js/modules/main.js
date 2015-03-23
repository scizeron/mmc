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
   role: 'anonymous',
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
}).run(function($rootScope, $location, userService, utils) {
 $rootScope.$on( "$routeChangeStart", function(event, next, current) {
  utils.debug('$routeChangeStart, next: ' + JSON.stringify(next));
  var nextPath = (typeof(next.$$route) != 'undefined') ? next.$$route.originalPath : '/home';
  if (nextPath == '/logout') {
   return;
  }
  
  var expectedRole = (typeof(next.$$route) != 'undefined' && typeof(next.$$route.role) != 'undefined') ? next.$$route.role : 'user';
  
  $rootScope.$broadcast('user', userService.getUser());
  
  if (nextPath == '' || nextPath == '/' || nextPath == '/home') {
   expectedRole = 'anonymous';
   $rootScope.app.jumbotron = true;
  }

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