'use strict';

angular.module('mmcApp').service('refValues', function(utils, $q, $http) {
 var countries = null;
 var nbTypeRange = null;
 var grades = null;
 var gradesValues = null;
 var years = null;
 
 return {
  getCountriesPromise: function() {
   if (countries != null) {
	utils.debug('The countries ref is already loaded');
    return countries.promise;	  
   }
   countries = $q.defer();
   $http.get("assets/country.json").success(function(response) {	
	utils.debug('The countries ref is loaded once.');   
	countries.resolve(response);   
   });
   return countries.promise; 	
  },
  getGradesPromise: function() {
   if (grades != null) {
	utils.debug('The grades ref is already loaded');	   
    return grades.promise;	  
   }
   grades = $q.defer();
   $http.get("assets/grades.json").success(function(response) {
	utils.debug('The grades ref is loaded once.');   	   
	gradesValues = response;    
    grades.resolve(response); 
   });
   return grades.promise;  
  },
  getGrade: function(value) {
   if (typeof(value) == 'undefined') {
	return null;	  
   }
   for (var i=0; i < gradesValues.length; i++) {
	utils.debug(gradesValues[i].value + '<>' + value);   
    if (gradesValues[i].value == value)	{
     utils.debug(value + ':' +  JSON.stringify(gradesValues[i])); 	
	 return gradesValues[i];  
    }
   }
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