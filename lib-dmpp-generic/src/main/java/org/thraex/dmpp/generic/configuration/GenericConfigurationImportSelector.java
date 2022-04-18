package org.thraex.dmpp.generic.configuration;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.thraex.dmpp.generic.doc.Documentation;
import org.thraex.toolkit.configuration.AuditorAwareConfiguration;
import org.thraex.toolkit.configuration.TemporalFormatConfiguration;
import org.thraex.toolkit.configuration.WrapperCodecsConfiguration;
import org.thraex.toolkit.exception.RestExceptionHandler;
import org.thraex.toolkit.webflux.routing.GenericRoutingConfiguration;

import java.util.stream.Stream;

/**
 * @author 鬼王
 * @date 2022/04/15 22:01
 */
public class GenericConfigurationImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return Stream.of(TemporalFormatConfiguration.class,
                AuditorAwareConfiguration.class,
                GenericRoutingConfiguration.class,
                Documentation.class,
                WrapperCodecsConfiguration.class,
                RestExceptionHandler.class).map(Class::getName).toArray(String[]::new);
    }

}
