var oauth2 = {
 step : undefined,
 'start' : function(redirectUri, apiScopes, gateway, userInfosCallback) {
  'use strict';
  var accessTokenMode = '';
  var stateOk = true;
  var error;
  var stateParameter, stateSession;
  var tokenId;
  var expiresIn;
  var userInfos, jwtInfos;
  var accessToken = webUtils.getSessionItem('oauth2.accessToken');

  if (accessToken != null) {
   accessTokenMode = 'session';
  } else {
   accessToken = webUtils.getParameter(window.location, 'access_token');
   if (accessToken != null) {
    accessTokenMode = 'request';
   }
  }
  
  if (accessToken != null) {
   webUtils.debug('use access_token from '  + accessTokenMode + ' : ' + accessToken);
  } else {
   webUtils.debug('no session/parameter access_token');
  }
  
  if (accessToken == null) {
   error = webUtils.getParameter(window.location, 'error');
   if (error != null) {
    webUtils.debug('error parameter : ' + error);
    oauth2.step = 'error';
   } else {
    webUtils.debug('no access_token, invoke /authorize');
    oauth2.step = 'doAuthorize';
    oauth2.authorize(redirectUri, apiScopes, gateway);
   }
  } else {
   oauth2.step = 'oauth2IsDone';
   
   if (accessTokenMode == 'session') {
    webUtils.debug('Use session infos, nothing to do.');
    
   } else if (accessTokenMode == 'request') {
    stateParameter = webUtils.getParameter(window.location, 'state');
    stateSession = webUtils.getSessionItem('oauth2.state'); 
    if (stateParameter != stateSession) {
     webUtils.error(stateParameter + '<> ' + stateSession);
     stateOk = false;
    }
    if (stateOk) {
     tokenId = webUtils.getParameter(window.location, 'token_id');
     if (tokenId != null) {
      jwtInfos = oauth2.checkTokenId(tokenId, accessToken);
      if (jwtInfos != null) {
       expiresIn = webUtils.getParameter(window.location, 'expires_in');
       webUtils.setSessionItem('oauth2.accessToken', accessToken, expiresIn);
       // uniquement jwtInfos
       userInfos = {
        sub : jwtInfos.sub,
        authentDate : jwtInfos.authentDate
       };
       webUtils.setSessionItem('oauth2.userInfos', userInfos, expiresIn);
       
       if (typeof(userInfosCallback) != 'undefined') {
    	webUtils.debug('Call userInfosCallback ...');
        userInfosCallback(expiresIn);   
       }
       
      } else {
       webUtils.error('OpenID Connect is failed, it\'s a security problem !!!');
      }       
     } else {
      webUtils.error('No token_id, first you have to send the required OpenID Connect scope in the authorize request.');
     }
    } else {
     webUtils.error('The state is ko, it\'s a security problem !!!');
    }
   } else {
    webUtils.error('Unknown accessTokenMode : ' + accessTokenMode);
   }
   
   webUtils.removeSessionItem('oauth2.state');
   webUtils.removeSessionItem('oauth2.nonce'); 
  }
 },
 'authorize' : function(redirectUri, apiScopes, gateway) {
  'use strict';
  var state, nonce;
  var scopes = env.get('oauth2.oidc.scopes');
  var authorizeUrl;
  
  if (apiScopes != null && apiScopes.length > 0) {
   scopes = apiScopes + ',' + scopes;
  }
  
  authorizeUrl = env.get('oauth2.url') + '/authorize' + '?client_id=' + encodeURIComponent(env.get('oauth2.client_id')) + '&response_type=token&scope=' + encodeURIComponent(scopes);
  
  if (typeof(gateway) != 'undefined' && gateway) {
   authorizeUrl = authorizeUrl + '&mode=' + encodeURIComponent('gateway');
  } else {
   state = webUtils.guid();
   nonce = webUtils.guid();
   authorizeUrl = authorizeUrl + '&state=' + encodeURIComponent(state) + '&nonce=' + encodeURIComponent(nonce);
   webUtils.setSessionItem('oauth2.state', state);
   webUtils.setSessionItem('oauth2.nonce', nonce);
  }
  
  authorizeUrl = authorizeUrl + '&redirect_uri=' + encodeURIComponent(redirectUri);
  
  webUtils.debug('---------------------------------------------------------------');
  webUtils.debug('  authorize');
  webUtils.debug('---------------------------------------------------------------');
  webUtils.debug('- uri           : ' + authorizeUrl);
  webUtils.debug('- scope         : ' + scopes);
  webUtils.debug('- redirect_uri  : ' + redirectUri);
  webUtils.debug('---------------------------------------------------------------');
    
  window.location.replace(authorizeUrl);
 },
 'checkTokenId' : function(tokenId, access_token) {
  'use strict';
  var jwtInfos;
  var jwt = new Jwt(tokenId, env.get('oauth2.oidc.verbose'));
  var result = jwt.checkSignature() && jwt.checkExpiration() && jwt.checkIssuer(env.get('oauth2.oidc.issuer')) && jwt.checkAudience(env.get('oauth2.client_id')) && jwt.checkAcessToken(access_token) && jwt.checkNonce(webUtils.getSessionItem('oauth2.nonce'));
  
  if (result) {
   webUtils.debug('OpenID Connect 1.0 : ' + result + ' [sub:' + jwt.getSub() + ', authentDate: ' + jwt.getAuthentDate() + ']');
   jwtInfos = {
    sub : jwt.getSub(),
    authentDate : jwt.getAuthentDate().getTime()
   };
   webUtils.debug('jwtInfos: ' + JSON.stringify(jwtInfos));
   return jwtInfos;
  } 
  
  webUtils.error('OpenID Connect 1.0 : ' + result);
  return null;
 } 
}