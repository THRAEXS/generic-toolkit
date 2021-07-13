package org.thraex.toolkit;

import org.junit.Assert;
import org.junit.Test;
import org.thraex.toolkit.util.Calculator;

/**
 * @author 鬼王
 * @date 2021/07/13 17:04
 */
public class CalculatorTest {

    @Test
    public void add() {
        Assert.assertEquals(49, Calculator.add(10, 39));
    }

    @Test
    public void minus() {
        Assert.assertEquals(-29, Calculator.minus(10, 39));
    }

}
