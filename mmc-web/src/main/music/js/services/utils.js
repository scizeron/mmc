'use strict';

var appendToLine = function(line, value, formatFunction) {
 if (value != null) {
  if (line.length > 0) {
   line += ', ';   
  } 
  if (typeof(formatFunction) != 'undefined') {
   line += formatFunction(value); 
  } else {
   line += value;
  }
 }
 return line;
};

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