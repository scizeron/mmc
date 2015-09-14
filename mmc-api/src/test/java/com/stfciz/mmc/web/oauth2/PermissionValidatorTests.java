package com.stfciz.mmc.web.oauth2;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author stfciz
 *
 * 14 sept. 2015
 */
public class PermissionValidatorTests {

  private PermissionValidator sut = new PermissionValidator();
  
  /**
   * 
   */
  @Test public void noExpectedScope() {
    Assert.assertThat(sut.validScopes(null,  null), CoreMatchers.is(true));
    Assert.assertThat(sut.validScopes(null,  new OAuth2Scope[0]), CoreMatchers.is(true));
    Assert.assertThat(sut.validScopes(new String[0], null), CoreMatchers.is(true));
    Assert.assertThat(sut.validScopes(new String[0], new OAuth2Scope[0]), CoreMatchers.is(true));
  }
  
  @Test public void validScopes() {
    Assert.assertThat(sut.validScopes(new String[] {"READ","WRITE"},  new OAuth2Scope[]{OAuth2Scope.WRITE}), CoreMatchers.is(true));
  }
  
  @Test public void validScopes2() {
    Assert.assertThat(sut.validScopes(new String[] {"WRITE","READ"},  new OAuth2Scope[]{OAuth2Scope.READ, OAuth2Scope.WRITE}), CoreMatchers.is(true));
  }
  
  @Test public void noValidScope() {
    Assert.assertThat(sut.validScopes(new String[] {"READ"},  new OAuth2Scope[]{OAuth2Scope.WRITE, OAuth2Scope.DELETE}), CoreMatchers.is(false));
  }
  
  @Test public void noValidScope2() {
    Assert.assertThat(sut.validScopes(new String[] {"WRITE","READ"},  new OAuth2Scope[]{OAuth2Scope.READ, OAuth2Scope.WRITE, OAuth2Scope.DELETE}), CoreMatchers.is(false));
  }
  
  @Test public void noExpectedUserRole() {
    Assert.assertThat(sut.validRoles(null,  null), CoreMatchers.is(true));
    Assert.assertThat(sut.validRoles(null,  new UserRole[0]), CoreMatchers.is(true));
    Assert.assertThat(sut.validRoles(new String[0], null), CoreMatchers.is(true));
    Assert.assertThat(sut.validRoles(new String[0], new UserRole[0]), CoreMatchers.is(true));
  }
  
  @Test public void validUserRole() {
    Assert.assertThat(sut.validRoles(new String[] {"DELETE"},  new UserRole[]{UserRole.ADMIN}), CoreMatchers.is(true));
  }
  
  @Test public void validUserRole2() {
    Assert.assertThat(sut.validRoles(new String[] {"READ", "WRITE"},  new UserRole[]{UserRole.WRITE}), CoreMatchers.is(true));
  }
}
