'use strict';

angular.module('mmcApp').directive('ngElevateZoom', function() {
  return {
    restrict: 'A',
    link: function(scope, element, attrs) {

      //Will watch for changes on the attribute
      attrs.$observe('zoomImage',function(){
        linkElevateZoom();
      })

      function linkElevateZoom(){
        //Check if its not empty
        if (!attrs.zoomImage) return;
        element.attr('data-zoom-image',attrs.zoomImage);
        //$(element).elevateZoom({'gallery' : 'gallery', 'galleryActiveClass': 'active', 'zoomType': 'inner', 'cursor' : 'crosshair'}); 
        $(element).elevateZoom({'gallery' : 'gallery', 'galleryActiveClass': 'active'});
        // selectionne la premiere image
        $('#gallery a').first().addClass('active');
      }

      linkElevateZoom();

    }
  };
});

angular.module('mmcApp').directive('ngElevateZoomLast', function() {
 return function (scope, element, attrs) {
  if (scope.$last=== true) {
   var zoom = $("#zoom");  
   zoom.ready(function () {
    console.log("last image, elevateZoom ...");
    zoom.elevateZoom({'gallery' : 'gallery', 'galleryActiveClass': 'active'}); 
    // selectionne la premiere image
    $('#gallery a').first().addClass('active');
   });
  }
 };
});