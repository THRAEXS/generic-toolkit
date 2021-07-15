package org.thraex.toolkit.constant;

import java.util.Arrays;

/**
 * @author 鬼王
 * @date 2021/07/14 17:12
 */
public enum Whether {

    YES(1),
    NO(0);

    private final int value;

    Whether(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static Whether valueOf(final int value) {
        return Arrays.stream(values())
                .filter(it -> it.value == value)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return this.value + " " + name();
    }

}
