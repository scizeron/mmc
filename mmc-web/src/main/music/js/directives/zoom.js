'use strict';

angular.module('mmcApp').directive('ngJetZoom', function() {
  return {
	restrict: 'A',
    link: function(scope, element, attrs) {
      attrs.$observe('largeImage',function(){
        linkElevateZoom();
      })

      function linkElevateZoom() {
    	if (typeof(attrs.src ) == "undefined") {
    	 return;
    	}
    	  
        var zoomInstance = scope.zoomInstance;
        if (typeof(zoomInstance) == "undefined") {
         var options = { 'lensAutoCircle' : true };
         zoomInstance = new JetZoom($('#zomImage'),options);
         scope.zoomInstance = zoomInstance;
         console.log("Create a new zoomInstance");
        } else {
         console.log("The zoomInstance already exists");	
        }
        console.log("attrs.src:" + attrs.src + ", " + "attrs.largeImage:" + attrs.largeImage);
        zoomInstance.loadImage(attrs.src, attrs.largeImage);
      }
    }
  };
});