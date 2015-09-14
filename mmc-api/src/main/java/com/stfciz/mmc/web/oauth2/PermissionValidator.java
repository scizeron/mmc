package com.stfciz.mmc.web.oauth2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * @author stfciz
 *
 * 14 sept. 2015
 */
public class PermissionValidator {
  
  /**
   * 
   * @param scopes
   * @param grantedScopes
   * @return
   */
  public boolean validScopes(String[] grantedScopes, OAuth2Scope[] expectedScopes) {
    
    if (expectedScopes == null || expectedScopes.length == 0) {
      return true;
    }
    
    if (grantedScopes == null || grantedScopes.length == 0) {
      return false;
    }
    
    Set<String> grantedScopesSet = new HashSet<String>(Arrays.asList(grantedScopes).stream().map(str -> str.toUpperCase()).collect(Collectors.toList()));
    return Arrays.asList(expectedScopes).stream().filter(scope -> grantedScopesSet.contains(scope.name())).count() == expectedScopes.length ? true : false;
  }

  /**
   * 
   * @param userRoles
   * @param expectedRoles
   * @return
   */
  public boolean validRoles(String[] userRoles, UserRole[] expectedRoles) {
    
    if (expectedRoles == null || expectedRoles.length == 0) {
      return true;
    }
    
    if (userRoles == null || userRoles.length == 0) {
      return false;
    }
    
    Set<String> userRolesSet = new HashSet<String>(Arrays.asList(userRoles).stream().map(str -> str.toUpperCase()).collect(Collectors.toList()));
    return Arrays.asList(expectedRoles).stream().filter(expectedUserRole -> UserRole.ADMIN.equals(expectedUserRole) || userRolesSet.contains(expectedUserRole.name())).count() == expectedRoles.length ? true : false;
  }
}
