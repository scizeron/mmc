package com.stfciz.clt.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


public class LoggingAspect {

  private static final Logger LOGGER       = LoggerFactory.getLogger(LoggingAspect.class);
  
  @Pointcut("within(com.stfciz.clt.web.controller..*) ")
  public void loggingControllerPointcut() {}
  
  @Around("loggingPointcut()")
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//    if (LOGGER.isDebugEnabled()) {
//      LOGGER.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
//              joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
//    }
    try {
      Object result = joinPoint.proceed();
      return result;
    } catch(Exception e) {
      throw e;
    }
    
  }
}
