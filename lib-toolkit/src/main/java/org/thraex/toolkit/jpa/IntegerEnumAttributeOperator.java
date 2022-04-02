package org.thraex.toolkit.jpa;

/**
 * @author 鬼王
 * @date 2021/07/22 12:40
 */
public interface IntegerEnumAttributeOperator extends EnumAttributeOperator<Integer> {

    static <X extends IntegerEnumAttributeOperator> X find(X[] values, Integer value) {
        return EnumAttributeOperator.find(values, value);
    }

}
