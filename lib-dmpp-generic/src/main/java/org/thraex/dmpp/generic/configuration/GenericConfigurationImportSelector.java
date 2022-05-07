package org.thraex.dmpp.generic.configuration;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author 鬼王
 * @date 2022/04/15 22:01
 */
public class GenericConfigurationImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return GenericConfigurationRegistrar.list().stream().map(Class::getName).toArray(String[]::new);
    }

}
