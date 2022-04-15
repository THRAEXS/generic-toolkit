package org.thraex.dmpp.generic;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.thraex.dmpp.generic.doc.Documentation;
import org.thraex.toolkit.configuration.AuditorAwareConfiguration;
import org.thraex.toolkit.configuration.TemporalFormatConfiguration;
import org.thraex.toolkit.exception.RestExceptionHandler;
import org.thraex.toolkit.webflux.routing.GenericRoutingConfiguration;

import java.util.List;

/**
 * @author 鬼王
 * @date 2022/04/15 22:16
 */
public class GenericConfigurationRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        List.of(TemporalFormatConfiguration.class,
                AuditorAwareConfiguration.class,
                GenericRoutingConfiguration.class,
                Documentation.class,
                RestExceptionHandler.class).forEach(it -> {
            GenericBeanDefinition definition = new GenericBeanDefinition();
            definition.setBeanClass(it);
            // TODO: First letter lowercase
            registry.registerBeanDefinition(it.getSimpleName(), definition);
        });
    }
}
