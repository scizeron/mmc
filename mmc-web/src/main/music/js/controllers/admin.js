'use strict';

angular.module('mmcApp')
.controller('adminCtrl',['$scope',function($scope) {
 $scope.$emit('jumbotron.show', false);   
 $scope.message = "admin";	
}]);