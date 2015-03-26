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
 this.hasNext = function() {
  if (this.index < this.pageSize - 1) {
   debug('hasNext: true : index ' + this.index + ' < ' +  (this.pageSize - 1));	  
   this.index++;
   debug('hasNext: true : next index : ' + this.index + ', page: ' + this.page);   
   return true;
  }
  if (this.page < this.totalPages - 1) {
   debug('hasNext: true : page ' + this.page + ' < ' +  (this.totalPages - 1));
   this.page = this.page + 1;
   this.index = 0;
   debug('hasNext: true : next page : ' + this.page + ', index: ' + this.index);
   return true;  
  }
  debug('hasNext: false : index : ' + this.index + '/' + this.index + ', page: ' + this.page + '/' + this.totalPages);
  return false;	 
 };
 this.hasPrevious = function() {
  if (this.index > 0) {
   debug('hasPrevious: true : index ' + this.index + ' > 0');	  	  
   this.index = this.index - 1;
   debug('hasPrevious: true : next index : ' + this.index + ', page: ' + this.page);
   return true;
  }
  
  if (this.page > 0) {
   debug('hasPrevious: true : page ' + this.page + ' > 0');	 
   this.page = this.page - 1;
   this.index = this.pageSize - 1;
   debug('hasPrevious: true : next page : ' + this.page + ', index: ' + this.index);
   return true;  
  }
  debug('hasPrevious: false : index : ' + this.index + '/' + this.index + ', page: ' + this.page + '/' + this.totalPages);
  return false;	 
 }; 
};

function newNavFromMusicListResponse(response, index, page) {
 var nav = new Nav();
 nav.type = 'music';
 nav.totalPages = response.totalPages;
 nav.page = response.page;
 nav.pageSize = (response.docs == null) ? 0 : response.docs.length;
 if (typeof(index) != 'undefined' && index > -1) {
  nav.index = index;
  nav.id = (response.docs != null && index < response.docs.length && response.docs[index] != null) ? response.docs[index].id : null;
 }
 debug('new nav : ' + JSON.stringify(nav)); 
 return nav;
};

function LoggedUser() {
 this.loggedInUser = false;
 this.loggedInAdminUser = false;
};

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
  }, 
  setNav : function(nav) {
   $rootScope.nav = nav; 
  }, 
  navMusicDoc : function(navCallback) {
   var newIndex = this.nav().index;
   var newPage = this.nav().page;
   musicService.getDocs(this.app().query, newPage, true, function(response) {
	utils.debug('resolve newIndex : ' + newIndex + ', newPage :' + newPage);
	navCallback(newNavFromMusicListResponse(response, newIndex, newPage));
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
  }
 }
}]);
