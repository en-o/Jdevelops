package cn.tannn.jdevelops.result.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrUtilsTest {

    @Test
    void isNotBlank() {
        assertTrue(StrUtils.isNotBlank("foo"));
        assertFalse(StrUtils.isNotBlank(""));
        assertFalse(StrUtils.isNotBlank(" "));
    }

    @Test
    void isBlank() {
        assertFalse(StrUtils.isBlank("foo"));
        assertTrue(StrUtils.isBlank(""));
        assertTrue(StrUtils.isBlank(" "));
    }

    @Test
    void length() {
        assertEquals(3, StrUtils.length("123"));
        assertEquals(0, StrUtils.length(""));
    }
}
