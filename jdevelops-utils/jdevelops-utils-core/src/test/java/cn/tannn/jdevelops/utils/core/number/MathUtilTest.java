package cn.tannn.jdevelops.utils.core.number;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MathUtilTest{

    @Test
    void  testCalculate() {
        assertEquals(MathUtil.calculate(3),5);
        assertEquals(MathUtil.calculate(2),3);
        assertEquals(MathUtil.calculate(1),1);
    }
}
