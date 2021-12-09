package cn.jdevelops.http.core;

import cn.jdevelops.enums.number.NumEnum;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.*;
import java.util.Enumeration;


/**
 * @author  谭宁
 */
@Slf4j
public class IpUtil {
   final static String UNKNOWN = "unKnown";

    /**
     * 获取有网关是 的真正客户端IP  - 未测试
     * @param request request
     * @return String
     */
    public static String getPoxyIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if ( null != ip && !UNKNOWN.equalsIgnoreCase(ip) ) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if ( index != -1 ) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if ( null != ip && !UNKNOWN.equalsIgnoreCase(ip) ) {
            return ip;
        }
        return request.getRemoteAddr();
    }



    /**
     *
     * 获取本地真正的IP地址，即获得有线或者无线WiFi地址。
     * 过滤虚拟机、蓝牙等地址
     * @author tn
     * @date  2020/4/21 23:44
     * @return java.lang.String
     */
    public static String getRealIp() throws SocketException {
        Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
                .getNetworkInterfaces();
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = allNetInterfaces
                    .nextElement();

            // 去除回环接口，子接口，未运行和接口
            if (netInterface.isLoopback() || netInterface.isVirtual()
                    || !netInterface.isUp()) {
                continue;
            }

            if (!netInterface.getDisplayName().contains("Intel")
                    && !netInterface.getDisplayName().contains("Realtek")) {
                continue;
            }
            Enumeration<InetAddress> addresses = netInterface
                    .getInetAddresses();
            /*System.out.println(netInterface.getDisplayName());*/
            while (addresses.hasMoreElements()) {
                InetAddress ip = addresses.nextElement();
                if (ip != null) {
                    // ipv4
                    if (ip instanceof Inet4Address) {
                        /* System.out.println("ipv4 = " + ip.getHostAddress());*/
                        return ip.getHostAddress();
                    }
                }
            }
            break;
        }

        return "127.0.0.1";
    }


    /**
     * 获得MAC地址
     * @author tn
     * @date  2020/4/21 23:47
     * @param ip ip
     * @return java.lang.String
     */

    public static String getMacAddress(String ip){
        String str;
        String macAddress = "";
        try {
            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
            InputStreamReader ir = new InputStreamReader(p.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (int i = 1; i < NumEnum.HUNDRED.getNum(); i++) {
                str = input.readLine();
                if (str != null) {
                    if (1 < str.indexOf("MAC Address")) {
                        macAddress = str.substring(str.indexOf("MAC Address") + 14);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return macAddress;
    }

    /**
     * ip转换
     *
     * @param domainName 域名
     * @return String
     */
    public static String ipConvert(String domainName) {
        String ip ;
        try {
            ip = InetAddress.getByName(domainName).getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return domainName;
        }
        return ip;
    }

    /**
     * 获取客户端IP地址
     *
     * @return String
     */
    public static String getIpAddrAndName() throws IOException {
        return InetAddress.getLocalHost().toString();
    }

    /**
     * 获取客户端IP地址
     *
     * @return String
     */
    public static String getIpAddr() throws IOException {
        return InetAddress.getLocalHost().getHostAddress();
    }
}
