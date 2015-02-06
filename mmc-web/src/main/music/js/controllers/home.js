'use strict';
angular.module('mmcApp')
.controller('homeCtrl', ['$scope', 
function ($scope) {
 $scope.$emit('jumbotron.show', true); 
}]);