package cn.tannn.jdevelops.utils.http;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IpUtilTest {

    @Test
    void getPoxyIp() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String remoteAddr = "192.168.1.100"; // 模拟的 IP 地址
        when(request.getRemoteAddr()).thenReturn(remoteAddr);
        assertEquals("192.168.1.100", IpUtil.getPoxyIp(request));
        assertNotEquals("192.168.1.12", IpUtil.getPoxyIpEnhance(request));
    }

    @Test
    void getPoxyIpEnhance() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String remoteAddr = "192.168.1.100"; // 模拟的 IP 地址
        when(request.getRemoteAddr()).thenReturn(remoteAddr);
        assertEquals("192.168.1.100", IpUtil.getPoxyIpEnhance(request));
        assertNotEquals("192.168.1.12", IpUtil.getPoxyIpEnhance(request));
    }

    @Test
    void getRealIp() throws SocketException {
        // 192.168.1.71
        System.out.println(IpUtil.getRealIp());
    }



    @Test
    void ipConvert() {
        // 153.3.238.110
        System.out.println(IpUtil.ipConvert("www.baidu.com"));
    }

    @Test
    void getIpAddrAndName() throws IOException {
        // tan/172.18.96.1
        System.out.println(IpUtil.getIpAddrAndName());
    }

    @Test
    void getIpAddr() throws IOException {
        // 172.18.96.1
        System.out.println(IpUtil.getIpAddr());
    }
}
