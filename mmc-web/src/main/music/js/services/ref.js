'use strict';

angular.module('mmcApp').service('refValues', function(utils, $q, $http) {
 var countries = null;
 var countriesValues = null;
 var nbTypeRange = null;
 var grades = null;
 var gradesValues = null;
 var years = null;
 var months = null;
 
 return {
  getColors: function() {
   return settings.music.colors;  
  },
  getCountriesPromise: function() {
   if (countries != null) {
	utils.debug('The countries ref is already loaded');
    return countries.promise;	  
   }
   countries = $q.defer();
   $http.get("settings/country.json").success(function(response) {	
	utils.debug('The countries ref is loaded once.');   
	countriesValues = response;
	countries.resolve(response);   
   });
   return countries.promise; 	
  },
  getCountries: function() {
   return countriesValues;  
  },
  getGradesPromise: function() {
   if (grades != null) {
	utils.debug('The grades ref is already loaded');	   
    return grades.promise;	  
   }
   grades = $q.defer();
   $http.get("settings/rating.json").success(function(response) {
	utils.debug('The grades ref is loaded once.');   	   
	gradesValues = response;    
    grades.resolve(response); 
   });
   return grades.promise;  
  },
  getGrades: function() {
   return gradesValues;  
  },
  getGrade: function(value) {
   if (typeof(value) == 'undefined') {
	return null;	  
   }
   for (var i=0; i < gradesValues.length; i++) {
    if (gradesValues[i].value == value)	{
	 return gradesValues[i];  
    }
   }
  },
  getGradeToString: function(value) {
   if (typeof(value) == 'undefined') {
	return null;	  
   }	  
   var grade = this.getGrade(value);
   return grade.code + ' - ' + grade.name;
  },
  getNbTypeRange: function() {
   if (nbTypeRange != null) {
	return nbTypeRange;  
   }	
   nbTypeRange = [];
   for (var i=1; i < settings.music.nbMaxType; i++) {
    nbTypeRange.push(i);
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
   for (var j=maxYear; j >= minYear; j--) {
    years.push(j);	 
   }
   return years;
  }, 
  getMonths: function() {
   if (months != null) {
	return months;  
   }
   months = [];
   for (var j=1; j <= 12; j++) {
	months.push(j);	 
   }
   return months;
  }
 };
});