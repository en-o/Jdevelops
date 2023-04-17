package cn.jdevelops.util.core.string;

import junit.framework.TestCase;

import java.util.Arrays;

public class StringCommonTest extends TestCase {

    public void testExtractIp() {
        assertEquals(StringCommon.extractIp("Current IP: 192.168.1.1, 2255.0.0.1 and 1.0.0.0"), Arrays.asList("192.168.1.1","1.0.0.0"));
        assertEquals(StringCommon.extractIp("Current IP: 192.168.1.1"),Arrays.asList("192.168.1.1"));
        assertEquals(StringCommon.extractIp("Current IP: 192.168.1.556"),Arrays.asList("192.168.1.55"));
    }
}
