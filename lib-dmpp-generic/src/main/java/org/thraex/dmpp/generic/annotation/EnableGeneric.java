package org.thraex.dmpp.generic.annotation;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.thraex.toolkit.configuration.AuditorAwareConfiguration;
import org.thraex.toolkit.exception.RestExceptionHandler;
import org.thraex.toolkit.configuration.TemporalFormatConfiguration;
import org.thraex.toolkit.webflux.routing.GenericRoutingConfiguration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 鬼王
 * @date 2022/04/14 21:49
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        TemporalFormatConfiguration.class,
        RestExceptionHandler.class,
        AuditorAwareConfiguration.class,
        GenericRoutingConfiguration.class
})
@Configuration
public @interface EnableGeneric {
}
