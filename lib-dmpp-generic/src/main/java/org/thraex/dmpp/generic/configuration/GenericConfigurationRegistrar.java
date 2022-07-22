package org.thraex.dmpp.generic.configuration;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.thraex.dmpp.generic.doc.Documentation;
import org.thraex.toolkit.configuration.AuditorAwareConfiguration;
import org.thraex.toolkit.configuration.HandlerRoutingConfiguration;
import org.thraex.toolkit.configuration.TemporalFormatConfiguration;
import org.thraex.toolkit.configuration.WrapperCodecsConfiguration;
import org.thraex.toolkit.exception.HandlerFunctionExceptionHandler;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        // TODO: Optimization
        Supplier<Class<?>> supplier = () -> ClassUtils.isPresent("org.springframework.data.domain.AuditorAware",
                null) ? AuditorAwareConfiguration.class : null;

        return Stream.of(
                TemporalFormatConfiguration.class,
                HandlerRoutingConfiguration.class,
                WrapperCodecsConfiguration.class,
                HandlerFunctionExceptionHandler.class,
                Documentation.class,
                supplier.get()).filter(Objects::nonNull).collect(Collectors.toList());
    }

}
