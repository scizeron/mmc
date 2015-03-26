var Jwt = function(jwtValue, verboseValue) {
 this.jwtValue = jwtValue;
 this.jwsHeader;
 this.jwsPayload;
 this.verbose = verboseValue;
  
 /**
  * _isArray (fix for IE prior to 9)
  */
 this._isArray = function(obj) {
  return Object.prototype.toString.call(obj) === '[object Array]';
 };
 
 /**
  * @name checkSignature
  * @function
  * @param {String} sJWS JWS signature string to be verified.
  * @return {Boolean} returns true when JWS signature is valid, otherwise returns false.
  * @throws if JWS Header is a malformed JSON string.
  */
 this.checkSignature = function() {
  this.debug("Signature: " + this.jwtValue);	 
	 
  var jws = new KJUR.jws.JWS(), 
      verifyJWSByNE;
	 	  
  if (this.jwtValue.match(/^([^.]+)\.([^.]+)\.([^.]+)$/) == null) {
   this.error("JWS signature is not a form of 'Head.Payload.SigValue'.");
   return false;
  }   

  try {	  
   this.jwsHeader = JSON.parse(b64utoutf8(RegExp.$1));
  } catch (ex) {
   this.error("b64utoutf8 error : " + ex.message);
   return false;
  }
  
  this.debug("Public Key Modulus (N): " + this.jwsHeader.jwk.n + ", Public Exponent (E): " + this.jwsHeader.jwk.e + ".");
    
  try {
   verifyJWSByNE = jws.verifyJWSByNE(this.jwtValue, b64utohex(this.jwsHeader.jwk.n), b64utohex(this.jwsHeader.jwk.e));
   if (!verifyJWSByNE) {
	this.error("verifyJWSByNE: " + verifyJWSByNE);
    return false;
   }
  } catch (ex) {
   this.error("verifyJWSByNE error : " + ex.message);
   return false;
  }

  this.jwsPayload = JSON.parse(jws.parsedJWS.payloadS);
  
  this.headB64U = jws.parsedJWS.headB64U;
  this.payloadB64U = jws.parsedJWS.payloadB64U;
  this.sigvalB64U = jws.parsedJWS.sigvalB64U;
  this.si = jws.parsedJWS.si;
  this.sigvalH = jws.parsedJWS.sigvalH;
  this.headS = jws.parsedJWS.headS;
  this.payloadS = jws.parsedJWS.payloadS;
  
  this.debug("Claims: " + JSON.stringify(this.jwsPayload));
 
  return true;
 };

 /**
  * @function
  * @return {String} returns the sub.
  */
 this.getSub = function() {
  return this.jwsPayload.sub;
 };
 
 /**
  * @function
  * @return {Date} returns the authent date (the number of seconds from 1970-01-01T0:0:0Z as measured in UTC until the date/time).
  */
 this.getAuthentDate = function() {
  return new Date(this.jwsPayload.auth_time * 1000);
 }; 
 
 /**
  * @name checkExpiration
  * @function
  * @return {Boolean} 
  */
 this.checkExpiration = function() {
  var nowInScs = Math.round((new Date().getTime() / 1000));
  
  if (nowInScs > this.jwsPayload.exp) {
   this.error("The ID Token is expired [actual:" + new Date(nowInScs*1000) + ", exp:" + new Date(this.jwsPayload.exp*1000) + "].");
   return false;  
  }
  this.debug("dates are ok.");
  return true;
 };
 
 /**
  * @name checkIssuer
  * @function
  * @param {String} issuer string to be verified.
  * @return {Boolean} returns true when issuer is valid, otherwise returns false. 
  */
 this.checkIssuer = function(issuer) {
  return this.checkValue("issuer", issuer, this.jwsPayload.iss);
 };

 /**
  * @name checkNonce
  * @function
  * @param {String} nonce string to be verified.
  * @return {Boolean} returns true when nonce is valid, otherwise returns false. 
  * @return {Boolean} 
  */
 this.checkNonce = function(nonce) {
  return this.checkValue("nonce", nonce, this.jwsPayload.nonce);
 };
 
 /**
  * @name checkSub
  * @function
  * @param {String} sub string to be verified.
  * @return {Boolean} returns true when sub is valid, otherwise returns false. 
  * @return {Boolean} 
  */
 this.checkSub = function(sub) {
  return this.checkValue("sub", sub, this.jwsPayload.sub);
 }; 
 
 /**
  * @name checkAudience
  * @function
  * @param {String} audience string to be verified.
  * @return {Boolean} returns true when audience is valid, otherwise returns false. 
  * @return {Boolean} 
  */
 this.checkAudience = function(audience) {
  var check = false, audToStr, i;
  
  if (this._isArray(this.jwsPayload.aud)) { 
   audToStr = this.jwsPayload.aud.toString();
   for(i = 0; i < this.jwsPayload.aud.length; i++) {
    if (this.jwsPayload.aud[i] == audience) {
     check = true;
     break;
    }
   }
  } else {
   audToStr = this.jwsPayload.aud;
   if (this.jwsPayload.aud == audience) {
    check = true;	  
   }
  }
  
  if (!check) {
   this.error("The audience is incorrect [actual:'" + audToStr + "', expected:" + audience + "'].");
   return false;
  }
  
  this.debug("audience is ok.");
  return true;
 }; 
 
 /**
  * @name checkAcessToken
  * @function
  * @param {String} accessToken string to be verified.
  * @return {Boolean} returns true when accessToken is valid, otherwise returns false.
  * @return {Boolean}  
  */
 this.checkAcessToken = function(accessToken) {
  var hash = CryptoJS.SHA256(accessToken), 
      hashHex = hash.toString(CryptoJS.enc.Hex),
      hashB64;
  hashHex = hashHex.substring(0, hashHex.length / 2);
  hashB64 = hex2b64(hashHex);
  return this.checkValue("at_hash", hashB64, this.jwsPayload.at_hash);
 }; 

 /**
  * @name checkValue
  * @function
  * @param {String} name string value name.
  * @param {String} value string to be verified.
  * @param {String} expected string to be compared.
  * @return {Boolean} returns true when values are the same, otherwise returns false. 
  */
 this.checkValue = function(name, value, expected) {
  if (value != expected) {
   this.error("The '" + name + "' is incorrect [actual:'"+ value + "', expected:'" + expected + "'].");
   return false;
  }	 
  this.debug(name + " is ok.");
  return true;	 
 };
 
 /**
  * 
  */
 this.debug = function(str) {
  if (this.verbose && typeof console != "undefined") {
   console.log("Jwt:"+str);
  } 
 };
 
 /**
  * 
  */
 this.error = function(str) {
  if (typeof console != "undefined") {
   console.error("Jwt:"+str);
  } 	  
 }; 
};