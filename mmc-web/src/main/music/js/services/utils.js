'use strict';

angular.module('mmcApp')
.factory('utils', [ 
function() {
 var verbose = {};
 return { 
  debug : function(msg) {
   if (typeof(console) != "undefined" && verbose) {
    console.log(msg);
   }  
  },
  error : function(msg) {
    if (typeof(console) != "undefined") {
     console.error(msg);
    }  
  }, 
  verbose : verbose
 };
}]);