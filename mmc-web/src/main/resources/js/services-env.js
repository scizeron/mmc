var servicesEnv = {
 oauth2 : {
  url : 'https://' + window.location.hostname + '/authz',
  client_id : 'mmc',
  oidc : {
   scopes : 'openid profile',
   issuer : 'demo_issuer',
   verbose : true
  }    
 }, 
 api : {
  url : 'https://' + window.location.hostname + '/mmc-api'
 }
};