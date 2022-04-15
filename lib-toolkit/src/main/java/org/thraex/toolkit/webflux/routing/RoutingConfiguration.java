package org.thraex.toolkit.webflux.routing;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 鬼王
 * @date 2022/04/14 22:51
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GenericRoutingConfiguration.class)
@Configuration(proxyBeanMethods = false)
public @interface RoutingConfiguration {
}
