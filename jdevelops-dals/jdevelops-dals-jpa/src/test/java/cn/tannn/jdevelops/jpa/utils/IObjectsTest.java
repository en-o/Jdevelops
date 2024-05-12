package cn.tannn.jdevelops.jpa.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IObjectsTest {

    @Test
    void nonNull() {
        assertFalse(IObjects.nonNull(null));
        assertFalse(IObjects.nonNull(""));
        assertTrue(IObjects.nonNull(" "));
        assertTrue(IObjects.nonNull("x"));
    }

    @Test
    void isNull() {
        assertTrue(IObjects.isNull(null));
        assertTrue(IObjects.isNull(""));
        assertTrue(IObjects.isNull(" "));
        assertFalse(IObjects.isNull("x"));
    }

    @Test
    void testIsNull() {
        assertTrue(IObjects.isNull("",true));
        assertTrue(IObjects.isNull(" ",true));
        assertTrue(IObjects.isNull(null,true));
        assertTrue(IObjects.isNull(null,false));
        assertFalse(IObjects.isNull(" ",false));
        assertFalse(IObjects.isNull("",false));
    }

    @Test
    void isaBoolean() {
        assertTrue(IObjects.isaBoolean(""));
        assertTrue(IObjects.isaBoolean(" "));
        assertTrue(IObjects.isaBoolean("null"));
    }

    @Test
    void isBlank() {
        assertTrue(IObjects.isBlank(""));
        assertTrue(IObjects.isBlank(" "));
        assertTrue(IObjects.isBlank("null"));
        assertFalse(IObjects.isBlank("a"));
    }
}
