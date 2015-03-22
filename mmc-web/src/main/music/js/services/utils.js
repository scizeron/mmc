'use strict';

var appendToLine = function(line, value, formatFunction) { 
 if (value != null) {
  var resValue = value;
  if (typeof(formatFunction) != 'undefined') {
   resValue = formatFunction(value); 
  }
  if (resValue.length > 0) {
   if (line != null && line.length > 0) {
    line += ', ' + resValue;   
   } else {
    line = resValue;	  
   }
  }
 }
 
 if (line == null) {
  return '';	 
 } else {
  return line;
 }
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