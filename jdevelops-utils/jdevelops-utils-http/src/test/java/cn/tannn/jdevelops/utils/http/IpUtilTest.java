package cn.tannn.jdevelops.utils.http;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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


    @Test
    void parseIpRange() throws UnknownHostException {
        String ipRange = "192.168.2.10-20";
        List<String> ipList = IpUtil.parseIpRange(ipRange);
        System.out.println("192.168.2.10-20:"+ipList);

        assertThrows(IllegalArgumentException.class,() ->{
            String ipRange2 = "192.168.2.240-256";
            IpUtil.parseIpRange(ipRange2);
        } );

        ipRange = "192.168.0.1-192.168.0.10";
        ipList = IpUtil.parseIpRange(ipRange);
        System.out.println("192.168.0.1-192.168.0.10:"+ipList);

        ipRange = "192.168.0.1-192.168.1.10";
        ipList = IpUtil.parseIpRange(ipRange);
        System.out.println("192.168.0.1-192.168.1.10:"+ipList);
    }


    @Test
    void isValidIpV4() {
        String[] testIps = {
                "192.168.0.1",
                "255.255.255.255",
                "0.0.0.0",
                "256.256.256.256", // 无效
                "192.168.0",       // 无效
                "192.168.0.256",   // 无效
                "192.168.0.-1",    // 无效
                "192.168.0.1.1",   // 无效
                "192.168.01.1",    // 有效，但通常建议使用0-9表示单个数字
                "192.168.0.01"     // 有效，但通常建议使用0-9表示单个数字
        };

        for (String ip : testIps) {
            System.out.println("IP: " + ip + " is valid: " + IpUtil.isValidIpV4(ip));
        }
    }

    @Test
    void validateIpOrRange() {
        String[] testInputs = {
                "192.168.0.1",
                "255.255.255.255",
                "0.0.0.0",
                "256.256.256.256", // 无效
                "192.168.0",       // 无效
                "192.168.0.256",   // 无效
                "192.168.0.-1",    // 无效
                "192.168.0.1.1",   // 无效
                "192.168.0.1-100",
                "192.168.0.1-192.168.0.100",
                "192.168.0.1-256", // 无效
                "192.168.0.1-192.168.0.256" // 无效
        };

        for (String input : testInputs) {
            int validated = IpUtil.validateIpOrRange(input);
            System.out.println("Input: " + input + " is " + validated);
        }
    }
}
