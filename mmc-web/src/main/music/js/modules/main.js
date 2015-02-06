'use strict';
angular.module('mmcApp', [
 'ngRoute'
 ,'ui.bootstrap'
])
.config(['$routeProvider', function($routeProvider) {
 $routeProvider.
  when('/home', {
   templateUrl: 'views/home.html',
   controller: 'homeCtrl'
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
   controller: 'musicEditViewCtrl'
  }).
  when('/music_view/:musicDocId', {
   templateUrl: 'views/music/view.html',
   controller: 'musicEditViewCtrl'
  }). 
  otherwise({
   redirectTo: '/home'
  });
}]);