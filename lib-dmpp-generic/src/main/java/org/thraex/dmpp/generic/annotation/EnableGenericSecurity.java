package org.thraex.dmpp.generic.annotation;

import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.thraex.dmpp.generic.security.WebFluxSecurityConfiguration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 鬼王
 * @date 2022/05/12 22:09
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(WebFluxSecurityConfiguration.class)
@EnableWebFluxSecurity
public @interface EnableGenericSecurity {
}
