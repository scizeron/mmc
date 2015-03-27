'use strict';

/**
 * 
 * @param msg
 */
function debug(msg) {
 if (settings.verbose && typeof(console) != "undefined") {
  console.log(msg);
 }	
};

/**
 * 
 * @param msg
 */
function error(msg) {
 if (typeof(console) != "undefined") {
  console.error(msg);
 }  
};

/**
 * 
 */
function App() {
 this.jumbotron = false;
 this.query = null;
 this.universe = null;
};

/**
 *  page  : [0, totalPages-1]
 *  index : [0, pageSize-1]
 */
function Nav() {
 this.totalPages = 0;
 this.page = 0;
 this.index = 0;
 this.pageSize = settings.pageSize;
 this.type = null;
 this.id = null;
 this.first = function() {
  this.index = 0;
  this.page = 0;
 },
 this.last = function() {
  this.index = this.pageSize;
  this.page = this.totalPages - 1;
 },
 this.hasNextPage = function() {
  if (this.page < this.totalPages - 1) {
   debug('hasNext: true : page ' + this.page + ' < ' +  (this.totalPages - 1));
   this.page = this.page + 1;
   this.index = 0;
   debug('hasNextPage: true : next page : ' + this.page + ', index: ' + this.index);
   return true;
  }
  debug('hasNextPage: false : index : ' + this.index + '/' + this.index + ', page: ' + this.page + '/' + this.totalPages);
  return false;
 },
 this.hasNext = function() {
  if (this.index < this.pageSize - 1) {
   debug('hasNext: true : index ' + this.index + ' < ' +  (this.pageSize - 1));	  
   this.index++;
   debug('hasNext: true : next index : ' + this.index + ', page: ' + this.page);   
   return true;
  }
  return this.hasNextPage();	 
 };
 this.hasPreviousPage = function(index) {
  if (this.page > 0) {
   debug('hasPrevious: true : page ' + this.page + ' > 0');	 
   this.page = this.page - 1;
   this.index = index;
   debug('hasPreviousPage: true : next page : ' + this.page + ', index: ' + this.index);
   return true;  
  }
  debug('hasPreviousPage: false : index : ' + this.index + '/' + this.index + ', page: ' + this.page + '/' + this.totalPages);
  return false;
 },
 this.hasPrevious = function() {
  if (this.index > 0) {
   debug('hasPrevious: true : index ' + this.index + ' > 0');	  	  
   this.index = this.index - 1;
   debug('hasPrevious: true : next index : ' + this.index + ', page: ' + this.page);
   return true;
  }
  return this.hasPreviousPage(this.pageSize - 1);
 }; 
};

/**
 * 
 * @param response
 * @param index
 * @param page
 * @returns {___anonymous2351_2353}
 */
function newNavFromMusicListResponse(response, index, page) {
 var nav = new Nav();
 nav.type = 'music';
 nav.totalPages = response.totalPages;
 nav.page = response.page;
 nav.pageSize = (response.docs == null) ? 0 : response.docs.length;
 if (typeof(index) != 'undefined' && index > -1) {
  if (index >= response.docs.length) {
   index = response.docs.length - 1;  
  }
  nav.id = (response.docs != null && index < response.docs[index] != null) ? response.docs[index].id : null;
  nav.index = index;
 }
 debug('new nav : ' + JSON.stringify(nav)); 
 return nav;
};

/**
 * 
 * @param line
 * @param value
 * @param formatFunction
 * @returns
 */
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

/**
 * 
 */
angular.module('mmcApp')
.factory('utils', [ 
function() {
 var verbose = {};
 return { 
  debug : function(msg) {
   debug(msg);
  },
  error : function(msg) {
   error(msg);  
  }, 
  verbose : verbose
 };
}]);

angular.module('mmcApp').factory('appService', [ '$rootScope', 'musicService', 'utils', '$q', function($rootScope, musicService, utils,  $q) {
 return { 
  newUser : function(loggedInUser, loggedInAdminUser) {
   return { 'loggedInUser' : loggedInUser, 'loggedInAdminUser' : loggedInAdminUser};  
  },
  init : function() {
   utils.debug('******************************** INIT APP ********************************');
   var app = $rootScope.app;
   $rootScope.app = new App();	
   $rootScope.nav = new Nav();
   $rootScope.user = this.newUser(false,false); 
   
   utils.debug('- app:' + JSON.stringify(this.app()));
   utils.debug('- nav:' + JSON.stringify(this.nav()));
   utils.debug('**************************************************************************');
  },
  user : function() {
   return $rootScope.user;
  },
  setUser : function(user) {
   if (user != null) {
	$rootScope.user = this.newUser(true, user.isAdmin());
   } else {
	$rootScope.user = this.newUser(false, false);    
   }
  },
  app : function() {
   return $rootScope.app; 
  },
  nav : function() {
   return $rootScope.nav; 
  }, 
  setNav : function(nav) {
   $rootScope.nav = nav; 
  }, 
  navMusicDoc : function(navCallback) {
   var newIndex = this.nav().index;
   var newPage = this.nav().page;
   musicService.getDocs(this.app().query, newPage, true, function(response) {
	var nav = newNavFromMusicListResponse(response, newIndex, newPage);
	$rootScope.nav = nav;
	navCallback(nav);
   });
  }, 
  nextMusicDoc : function(navCallback) {
   if (this.nav().hasNext()) {
    this.navMusicDoc(navCallback);
   }
  },
  previousMusicDoc : function(navCallback) {
   if (this.nav().hasPrevious()) {
    this.navMusicDoc(navCallback);
   }
  },
  nextMusicDocPage : function(navCallback) {
   if (this.nav().hasNextPage()) {  
	this.navMusicDoc(navCallback);   
   }	  
  },
  previousMusicDocPage : function(navCallback) {
   if (this.nav().hasPreviousPage(0)) {  
	this.navMusicDoc(navCallback);   
   }
  },   
  firstMusicDocPage : function(navCallback) {
   this.nav().first();
   this.navMusicDoc(navCallback);  
  },
  lastMusicDocPage : function(navCallback) {
   this.nav().last();
   this.navMusicDoc(navCallback);	  
  }
 }
}]);
