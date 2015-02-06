package com.stfciz.clt;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan(basePackages={"com.stfciz.clt"})
@EnableAspectJAutoProxy(proxyTargetClass=true)
@ImportResource({"classpath:applicationContext-clt-core.xml"})
public class AppSpringCoreConfiguration {
  /** EMPTY **/
}