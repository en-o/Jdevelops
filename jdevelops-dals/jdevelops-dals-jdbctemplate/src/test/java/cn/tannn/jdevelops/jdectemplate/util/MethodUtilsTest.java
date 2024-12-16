package cn.tannn.jdevelops.jdectemplate.util;

import cn.tannn.jdevelops.jdectemplate.util.bean.TestDemoImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MethodUtilsTest {

    @Test
    void checkLocalMethod()  throws NoSuchMethodException {
        // 不能处理重写方法
        assertFalse(MethodUtils.checkLocalMethod(TestDemoImpl.class.getMethod("toString")));
        assertTrue(MethodUtils.checkLocalMethod(TestDemoImpl.class.getMethod("hashCode")));
        assertFalse(MethodUtils.checkLocalMethod(TestDemoImpl.class.getMethod("hi")));
    }

    @Test
    void testCheckLocalMethod() {
        assertTrue(MethodUtils.checkLocalMethod("toString"));
        assertTrue(MethodUtils.checkLocalMethod("hashCode"));
        assertFalse(MethodUtils.checkLocalMethod("hi"));
    }
}
