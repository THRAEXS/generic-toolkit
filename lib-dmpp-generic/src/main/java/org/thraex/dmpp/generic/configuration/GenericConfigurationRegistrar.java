package org.thraex.dmpp.generic.configuration;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.thraex.dmpp.generic.doc.Documentation;
import org.thraex.toolkit.configuration.AuditorAwareConfiguration;
import org.thraex.toolkit.configuration.GenericRoutingConfiguration;
import org.thraex.toolkit.configuration.TemporalFormatConfiguration;
import org.thraex.toolkit.configuration.WrapperCodecsConfiguration;
import org.thraex.toolkit.exception.RestExceptionHandler;

import java.util.List;

/**
 * Spare
 *
 * @author 鬼王
 * @date 2022/04/15 22:16
 */
public class GenericConfigurationRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry,
                                        BeanNameGenerator importBeanNameGenerator) {
        list().forEach(it -> {
            GenericBeanDefinition definition = new GenericBeanDefinition();
            definition.setBeanClass(it);
            String beanName = importBeanNameGenerator.generateBeanName(definition, registry);
            registry.registerBeanDefinition(beanName, definition);
        });
    }

    public static List<Class<?>> list() {
        return List.of(TemporalFormatConfiguration.class,
                AuditorAwareConfiguration.class,
                GenericRoutingConfiguration.class,
                WrapperCodecsConfiguration.class,
                RestExceptionHandler.class,
                Documentation.class);
    }

}
