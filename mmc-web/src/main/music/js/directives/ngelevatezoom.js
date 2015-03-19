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
        $(element).elevateZoom({'gallery' : 'gallery', 'galleryActiveClass': 'active', 'zoomType': 'inner', 'cursor' : 'crosshair'});
        // selectionne la premiere image
        $('#gallery a').first().addClass('active');
      }

      linkElevateZoom();

    }
  };
});
