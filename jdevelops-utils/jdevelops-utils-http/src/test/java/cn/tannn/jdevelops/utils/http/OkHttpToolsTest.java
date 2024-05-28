package cn.tannn.jdevelops.utils.http;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;


class OkHttpToolsTest {

    @Test
    void DEF() throws IOException {
        assertFalse(OkHttpTools.DEF().get("http://www.baidu.com").isEmpty());
    }

}

