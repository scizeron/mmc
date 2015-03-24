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

angular.module('mmcApp').factory('appService', [ '$rootScope', 'utils', function($rootScope, utils) {
 return { 
  initApp : function() {
   var app = $rootScope.app;
   if (typeof(app) == 'undefined') {
	$rootScope.app = {};	
	utils.debug("************************ INIT *********************************");
   }
  },
  setUser : function(user) {
   $rootScope.user = {
	 loggedInUser : (user != null) ? true : false
	,loggedInAdminUser : (user != null) ? user.isAdmin() : false
   };
  },
  setJumbotron : function(value) {
   $rootScope.app.jumbotron = value; 
  },
  getJumbotron : function() {
   return $rootScope.app.jumbotron; 
  },
  setQuery : function(value) {
   $rootScope.app.query = value; 
  },
  getQuery : function() {
   return $rootScope.app.query; 
  },
  setUniverse : function(value) {
   $rootScope.app.universe = value; 
  },
  getUniverse : function() {
   return $rootScope.app.universe; 
  }  
 };
}]);
