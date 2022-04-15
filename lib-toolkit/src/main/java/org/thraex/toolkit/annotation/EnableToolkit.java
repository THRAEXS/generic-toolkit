package org.thraex.toolkit.annotation;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.thraex.toolkit.configuration.AuditorAwareConfiguration;
import org.thraex.toolkit.exception.RestExceptionHandler;
import org.thraex.toolkit.configuration.TemporalFormatConfiguration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 鬼王
 * @date 2022/04/06 16:47
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ TemporalFormatConfiguration.class, RestExceptionHandler.class, AuditorAwareConfiguration.class })
@Configuration
@Deprecated
public @interface EnableToolkit {
}
