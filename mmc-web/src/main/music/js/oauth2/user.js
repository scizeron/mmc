var User = function(firstName, lastName, roles) {
 this.firstName = firstName;
 this.lastName = lastName;
 this.roles = roles;
 this.hasRole = function(expectedRole) {
  if (this.roles == null) {
   return false;
  }
  var result = false;
  this.roles.forEach(function(role) {
   if ((role.toLowerCase() == 'admin' || role.toLowerCase() == expectedRole.toLowerCase()) && !result) {
    result = true;
   }
  });
  return result;  
 };
 this.isAdmin = function() {
  return this.hasRole('admin');	 
 };
 this.toString = function() {
  return this.firstName + ', ' + this.lastName + ', "' +  this.roles + '", admin: ' + this.isAdmin();
 };
};