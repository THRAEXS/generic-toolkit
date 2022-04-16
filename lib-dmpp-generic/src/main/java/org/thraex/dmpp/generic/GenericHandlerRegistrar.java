package org.thraex.dmpp.generic;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.ChildBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.thraex.toolkit.webflux.handler.GenericHandler;

/**
 * @author 鬼王
 * @date 2022/04/16 21:57
 */
public class GenericHandlerRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry,
                                        BeanNameGenerator importBeanNameGenerator) {
//        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry, importBeanNameGenerator);
//        new ChildBeanDefinition();
        ChildBeanDefinition childBeanDefinition = new ChildBeanDefinition(GenericHandler.class.getName());
        System.out.println(1);
    }

}
