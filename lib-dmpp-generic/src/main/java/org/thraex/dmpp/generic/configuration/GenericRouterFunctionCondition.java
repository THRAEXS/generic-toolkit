package org.thraex.dmpp.generic.configuration;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.thraex.dmpp.generic.annotation.EnableGeneric;

/**
 * TODO: {@link EnableGeneric#genericRouterFunction()}
 *
 * @author 鬼王
 * @date 2022/04/23 17:39
 */
public class GenericRouterFunctionCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return true;
    }

}
