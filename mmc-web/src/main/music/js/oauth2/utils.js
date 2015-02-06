var webUtils = {
 'guid' : function() {
  function s4() {
   return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
  }
  return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
 },
 'newSessionItem' : function( value, ttl) {
  var item;
  if (typeof(ttl) != "undefined") {
   item = {
    v: value, 
    t: new Date().getTime(), 
    l: ttl * 1000
   };
  } else {
   item = {
    v: value
   };
  }
  return item;
 },
 'getParameter' : function(url, name) {
  'use strict';
	name = name.replace(/[\[]/, '\\\[').replace(/[\]]/, '\\\]');
	var regexS = '[\\#&]' + name + '=([^&#]*)';
	var regex = new RegExp(regexS);
	var results = regex.exec(url);
	if (results != null) {
	 return results[1];
	}
  return null;
 },
 'debug' : function(msg) {
  'use strict';
  if (typeof(console) != "undefined") {
   console.log(msg);
  }
 }, 
 'error' : function(msg) {
  'use strict';
  if (typeof(console) != "undefined") {
   console.error(msg);
  }
 },
 'setSessionItem' : function(key, value, ttl) {
  'use strict';
  var item = webUtils.newSessionItem(value, ttl);
  var jsonItem = JSON.stringify(item);
  sessionStorage.setItem(key, jsonItem);
 },
 'getSessionItem' : function(key) {
  'use strict';
   var item;
   var jsonItem = sessionStorage.getItem(key);
   if (jsonItem == null) {
    return null;
   }
   item = JSON.parse(jsonItem);
   if (typeof(item.l) == "undefined") {
    return item.v;
   }
   if ((item.t + item.l) > new Date().getTime() ) {
    return item.v;
   }
   // item expire
   webUtils.debug("\"" + key + "\" is expired");
   webUtils.removeSessionItem(key);
   return null;
 },
 'removeSessionItem' : function(key) {
  'use strict';
  sessionStorage.removeItem(key);
 },
 'clearSession' : function() {
  'use strict';
  sessionStorage.clear();
 },
 'displayDate' : function(time) {
  return new Date(time).toLocaleString();
 }
};