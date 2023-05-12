package cn.jdevelops.util.http;

import cn.jdevelops.util.http.entity.PingContextRule;
import cn.jdevelops.util.http.entity.PingVO;
import junit.framework.TestCase;
public class PingUtilTest extends TestCase {

    public void testPing() {
        assertEquals(PingUtil.ping("http://www.baidu.com"),1);
        assertEquals(PingUtil.ping("http://www.baidu.comcc"),0);
    }

    public void testTestPing() {
        assertEquals(PingUtil.ping("http://www.baidu.com",new PingContextRule("百度一下",1)),
                new PingVO(1,1));
        assertEquals(PingUtil.ping("http://www.baidu.com",new PingContextRule("tann",1)),
                new PingVO(1,2));
        assertEquals(PingUtil.ping("http://www.baidu.comcc",new PingContextRule("tann",1)),
                new PingVO(0,2));
    }
}
