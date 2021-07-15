package org.thraex.toolkit;

import org.junit.Assert;
import org.junit.Test;
import org.thraex.toolkit.constant.Whether;

/**
 * @author 鬼王
 * @date 2021/07/15 08:41
 */
public class WhetherTest {

    @Test
    public void test() {
        Assert.assertEquals(0, Whether.YES.ordinal());
        Assert.assertEquals(1, Whether.NO.ordinal());

        Assert.assertEquals(1, Whether.YES.value());
        Assert.assertEquals(0, Whether.NO.value());

        Assert.assertEquals("YES", Whether.YES.name());
        Assert.assertEquals("NO", Whether.NO.name());

        Assert.assertEquals("1 YES", Whether.YES.toString());
        Assert.assertEquals("0 NO", Whether.NO.toString());
    }

    @Test
    public void test1() {
        System.out.println(Whether.valueOf("YES"));
        System.out.println(Whether.valueOf("NO"));
        System.out.println(Whether.valueOf(1));
        System.out.println(Whether.valueOf(0));
        System.out.println(Whether.valueOf(2));
    }

}
