var env = {
 selected : 'dev.local',		
 ids : [
  {
   id : 'dev.local',
   oauth2: {
    url: 'http://localhost:3000/authz',
    client_id : 'test',
    oidc : {
     scopes : 'openid profile',
     issuer : 'demo_issuer',
     verbose : true
    }
   },
   api : {
    url : 'http://localhost:4000/mmc-api' 	   
   }
  },
  {
   id : 'dev.aws',	  
   oauth2 : {
    url : 'https://' + window.location.hostname + '/authz',
    client_id : 'test',
    oidc : {
     scopes : 'openid profile',
     issuer : 'demo_issuer',
     verbose : true
    }    
   }, 
   api : {
    url : 'https://' + window.location.hostname + '/mmc'
   }
  }
 ],
 'get' : function(property) {
   var ev;	 
   for (var i=0; i<env.ids.length; i++) {
	if (env.ids[i].id ==  env.selected) {
	 if (typeof(property) != 'undefined') {
	  return eval('env.ids[i].' + property);
	 } else { 	
	  return env.ids[i];
	 }
	}
	return null;
   }	 
 }
};