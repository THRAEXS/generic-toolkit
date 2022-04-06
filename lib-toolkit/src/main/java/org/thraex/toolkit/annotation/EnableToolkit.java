package org.thraex.toolkit.annotation;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.thraex.toolkit.configuration.AuditorAwareConfig;
import org.thraex.toolkit.configuration.RestExceptionHandler;
import org.thraex.toolkit.configuration.TemporalFormatConfig;

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
@Import({ TemporalFormatConfig.class, RestExceptionHandler.class, AuditorAwareConfig.class })
@Configuration
public @interface EnableToolkit {
}
