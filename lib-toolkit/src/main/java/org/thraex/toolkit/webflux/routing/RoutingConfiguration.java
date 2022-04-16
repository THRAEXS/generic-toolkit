package org.thraex.toolkit.webflux.routing;

import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Remove {@code @Import(GenericRoutingConfiguration.class)}.
 * <br>
 * Import ({@link GenericRoutingConfiguration}) separately.
 *
 * @author 鬼王
 * @date 2022/04/14 22:51
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration(proxyBeanMethods = false)
@Deprecated
public @interface RoutingConfiguration {
}
