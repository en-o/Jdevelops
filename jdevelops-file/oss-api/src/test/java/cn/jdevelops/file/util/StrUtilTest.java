package cn.jdevelops.file.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class StrUtilTest {

    @Test
    public void notBlank() {
        assertTrue(StrUtil.notBlank("123"));
        assertFalse(StrUtil.notBlank(""));
        assertFalse(StrUtil.notBlank(null));
    }

    @Test
    public void isBlank() {
        assertFalse(StrUtil.isBlank("123"));
        assertTrue(StrUtil.isBlank(""));
        assertTrue(StrUtil.isBlank(null));
    }
}
