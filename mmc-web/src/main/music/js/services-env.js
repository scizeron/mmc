var servicesEnv = {
 oauth2: {
  url: 'http://localhost:3000/authz',
  client_id : 'mmc',
  oidc : {
   scopes : 'openid profile',
   issuer : 'demo_issuer',
   verbose : true
  }
 },
 api : {
  url : 'http://localhost:4000/mmc-api' 	   
 }
};