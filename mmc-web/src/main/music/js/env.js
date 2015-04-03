var env = {
 'get' : function(property) {
   if (typeof(property) != 'undefined') {
	return eval('servicesEnv.' + property);
   }
   return null;
 }
};