package cn.jdevelops.util.core.list;

import junit.framework.TestCase;


public class CollectionUtilTest extends TestCase {

    public void testIsEmpty() {
        assertFalse(CollectionUtil.isEmpty(new String[]{"1","2"}));
        assertTrue(CollectionUtil.isEmpty(new String[0]));
        assertFalse(CollectionUtil.isEmpty(new Integer[]{1,2}));
        assertTrue(CollectionUtil.isEmpty(new Integer[0]));
    }
}
