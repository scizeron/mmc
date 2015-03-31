'use strict';

angular.module('mmcApp')
.directive('uploadFile', ['utils', function (utils) {
 return {
  restrict: 'A',
  transclude: 'true',
  scope: {
   files: '='
  },
  link: function(scope, element, attrs) {
	 element.bind('change', function(){
	  var file = element[0].files[0];
	  var fileItem = {
	   'file' : file,
	   'status' : 'i'
	  };
	  scope.files.push(fileItem);
	  utils.debug('files :  ' + scope.files.length);
	  scope.$apply();
	 });
	}
 }
}]);

angular.module('mmcApp')
.directive('ngColorPicker', function() {
 return {
  scope: {
   selected: '=',
   customizedColors: '=colors'
  },
  restrict: 'AE',
   template: '<ul><li ng-repeat="color in colors" ng-class="{selected: (color===selected)}" ng-click="pick(color)" style="background-color:{{color}};"></li></ul>',
   link: function (scope, element, attr) {
    scope.colors = scope.customizedColors;
    scope.selected = scope.selected || scope.colors[0];
    scope.pick = function (color) {
     scope.selected = color;
    };
   }
 };
});

angular.module('mmcApp')
.directive('htmldiv', function($compile, $parse) {
 return {
  restrict: 'E',
  link: function(scope, element, attr) {
   scope.$watch(attr.content, function() {
    element.html($parse(attr.content)(scope));
    $compile(element.contents())(scope);
   }, true);
  }
 }
});
