'use strict';

angular.module('mmcApp').service('refValues', function(utils, $q, $http) {
 var countries = null;
 var nbTypeRange = null;
 var grades = null;
 var years = null;
 
 return {
  getCountriesPromise: function() {
   if (countries != null) {
	utils.debug('The ref countries are already loaded');
    return countries.promise;	  
   }
   countries = $q.defer();
   $http.get("assets/country.json").success(function(response) {	
	utils.debug('The ref countries are loaded once.');   
	countries.resolve(response);   
   });
   return countries.promise; 	
  },
  getGradesPromise: function() {
   if (grades != null) {
    return grades.promise;	  
   }
   grades = $q.defer();
   $http.get("assets/grades.json").success(function(response) { 
    grades.resolve(response); 
   });
   return grades.promise;  
  },
  getNbTypeRange: function() {
   if (nbTypeRange != null) {
	return nbTypeRange;  
   }	
   nbTypeRange = [];
   for (var i=1; i < settings.music.nbMaxType; i++) {
    if ( i < 10) {
	 nbTypeRange.push("0" + i);	 
    } else {
	 nbTypeRange.push(i);
    }
   }   
   return nbTypeRange;
  },
  getYears: function() {
   if (years != null) {
	return years;  
   }
   var minYear = settings.music.minYear; 
   var maxYear = new Date().getFullYear();
   years = [];
   for (var j=minYear; j <= maxYear; j++) {
    years.push(j);	 
   }
   return years;
  }
 };
});