package org.thraex.toolkit.constant;

import org.thraex.toolkit.jpa.IntegerEnumAttributeOperator;

/**
 * @author 鬼王
 * @date 2021/07/14 17:12
 */
public enum Whether implements IntegerEnumAttributeOperator {

    YES(1),
    NO(0);

    private final int value;

    Whether(int value) {
        this.value = value;
    }

    @Override
    public Integer value() {
        return this.value;
    }

    public static Whether of(Integer value) {
        return IntegerEnumAttributeOperator.find(values(), value);
    }

    @Override
    public String toString() {
        return String.format("%s %d", name(), value);
    }

}
