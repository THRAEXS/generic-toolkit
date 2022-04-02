package org.thraex.toolkit.util;

/**
 * @author 鬼王
 * @date 2021/09/02 15:32
 */
public abstract class StringUtils {

    public static String repeat(final String str, final int repeat) {
        return new String(new char[repeat]).replace("\0", str);
    }

}
