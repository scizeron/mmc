'use strict';

function App() {
 this.jumbotron = false;
 this.query = null;
 this.universe = null;
};

/**
 *  currentPage : [0, totalPages-1]
 *  currentIndex : [0, pageSize-1]
 */
function Nav() {
 this.totalPages = 0;
 this.currentPage = 0;
 this.currentIndex = 0;
 this.pageSize = settings.pageSize;
 this.type = null;
 this.hasNext = function() {
  if (this.currentIndex < this.pageSize - 1) {
   this.currentIndex++;
   return true;
  }
  if (this.currentPage < this.totalPages - 1) {
   this.currentPage = this.currentPage + 1;
   this.currentIndex = 0;
   return true;  
  }
  return false;	 
 };
 this.hasPrevious = function() {
  if (this.currentIndex > 0) {
   this.currentIndex = this.currentIndex - 1;
   return true;
  }
  
  if (this.currentPage > 0) {
   this.currentPage = this.currentPage - 1;
   this.currentIndex = this.pageSize - 1;
   return true;  
  }
  
  return false;	 
 }; 
};

function LoggedUser() {
 this.loggedInUser = false;
 this.loggedInAdminUser = false;
}

function appendToLine(line, value, formatFunction) { 
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
  init : function() {
   utils.debug('******************************** INIT APP ********************************');
   var app = $rootScope.app;
   $rootScope.app = new App();	
   $rootScope.nav = new Nav();
   $rootScope.user = new LoggedUser();
   
   utils.debug('- app:' + JSON.stringify(this.app()));
   utils.debug('- nav:' + JSON.stringify(this.nav()));
   utils.debug('**************************************************************************');
  },
  user : function() {
   return $rootScope.user;
  },
  setUser : function(user) {
   if (user != null) {
	$rootScope.user.loggedInUser = true;
	$rootScope.user.loggedInAdminUser = user.isAdmin();
   } else {
	$rootScope.user = new LoggedUser();    
   }
  },
  app : function() {
   return $rootScope.app; 
  },
  nav : function() {
   return $rootScope.nav; 
  } 
 };
}]);
