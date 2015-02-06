'use strict';

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