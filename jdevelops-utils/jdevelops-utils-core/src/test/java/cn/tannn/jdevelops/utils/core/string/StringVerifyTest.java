package cn.tannn.jdevelops.utils.core.string;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringVerifyTest {

    @Test
     void testVerifyIp() {
        assertFalse(StringVerify.verifyIp("1.1.1.a"));
        assertTrue(StringVerify.verifyIp("127.0.0.1"));
        assertTrue(StringVerify.verifyIp("192.168.1.10"));
        assertTrue(StringVerify.verifyIp("172.168.0.2"));
        assertFalse(StringVerify.verifyIp("192.168.0.568"));
    }
}
