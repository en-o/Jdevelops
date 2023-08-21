package cn.jdevelops.util.core.number;

import junit.framework.TestCase;


public class MathUtilTest extends TestCase {

    public void testCalculate() {
        assertEquals(MathUtil.calculate(3),5);
        assertEquals(MathUtil.calculate(2),3);
        assertEquals(MathUtil.calculate(1),1);
    }
}
