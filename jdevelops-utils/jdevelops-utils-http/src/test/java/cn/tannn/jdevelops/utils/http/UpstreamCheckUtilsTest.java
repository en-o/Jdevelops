package cn.tannn.jdevelops.utils.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpstreamCheckUtilsTest {

    @Test
    void checkUrl() {
        assertFalse(UpstreamCheckUtils.checkUrl(""));
        assertTrue(UpstreamCheckUtils.checkUrl("http://www.baidu.com"));
    }


}
