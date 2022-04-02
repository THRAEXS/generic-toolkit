package org.thraex.toolkit.jpa;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author 鬼王
 * @date 2021/07/22 12:08
 */
public interface EnumAttributeOperator<Y> {

    Y value();

    static <X extends EnumAttributeOperator, Y> X find(X[] values, Y value) {
        if (Objects.isNull(values) || Objects.isNull(value)) { return null; }

        return Stream.of(values)
                .filter(it -> it.value().equals(value))
                .findFirst()
                .orElse(null);
    }

}
