package cn.tannn.jdevelops.apis.log.util;


import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IpUtilTest {

    @org.junit.jupiter.api.Test
    void printParams() {
        // https://www.yuque.com/tanning/mbquef/vv5ddq
        Set<String> strings = Set.of("/a/c","/a/b","/ta/**","/tb/*","/g?r");
        assertFalse(IpUtil.printParams(strings,"/w"));
        assertTrue(IpUtil.printParams(strings,"/a/c"));

        assertTrue(IpUtil.printParams(strings,"/ta/x/a"));
        assertTrue(IpUtil.printParams(strings,"/ta/v"));

        assertTrue(IpUtil.printParams(strings,"/tb/ada"));
        assertFalse(IpUtil.printParams(strings,"/tb/ada/asda"));

        assertTrue(IpUtil.printParams(strings,"/gar"));
        assertFalse(IpUtil.printParams(strings,"/gaar"));

    }
}
