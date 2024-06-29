package cn.tannn.jdevelops.utils.http;

import cn.tannn.jdevelops.utils.http.pojo.PingContextRule;
import cn.tannn.jdevelops.utils.http.pojo.PingVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PingUtilTest {

    @Test
    void ping() {
        assertEquals(PingUtil.ping("http://www.baidu.com"),1);
        assertEquals(PingUtil.ping("http://www.baidu.comcc"),0);
    }

    @Test
    void testPing() {
        assertEquals(PingUtil.ping("http://www.baidu.com",new PingContextRule("百度一下",1)),
                new PingVO(1,1));
        assertEquals(PingUtil.ping("http://www.baidu.com",new PingContextRule("tann",1)),
                new PingVO(1,2));
        assertEquals(PingUtil.ping("http://www.baidu.comcc",new PingContextRule("tann",1)),
                new PingVO(0,2));
    }
}
