package cn.jdevelops.jap.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class IObjectsTest {

    @Test
    public void nonNull() {
        assertFalse(IObjects.nonNull(""));
        assertTrue(IObjects.nonNull("ada"));
    }

    @Test
    public void isNull() {
        assertTrue(IObjects.isNull(""));
        assertFalse(IObjects.isNull("ad"));
    }

    @Test
    public void isaBoolean() {
        assertTrue(IObjects.isaBoolean(null));
        assertTrue(IObjects.isaBoolean(""));
        assertTrue(IObjects.isaBoolean("null"));
        assertFalse(IObjects.isaBoolean("ad"));
    }

    @Test
    public void isBlank() {
        assertTrue(IObjects.isBlank(null));
        assertTrue(IObjects.isBlank(""));
        assertTrue(IObjects.isBlank("null"));
        assertFalse(IObjects.isBlank("ad"));
    }
}
