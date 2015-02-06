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
}])
;
