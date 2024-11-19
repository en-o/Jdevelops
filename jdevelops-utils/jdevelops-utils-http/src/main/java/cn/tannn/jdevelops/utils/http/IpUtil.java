package cn.tannn.jdevelops.utils.http;

import cn.tannn.jdevelops.utils.http.pojo.NumEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.*;
import java.util.Enumeration;


/**
 * @author 谭宁
 */
public class IpUtil {

    private static final Logger LOG = LoggerFactory.getLogger(IpUtil.class);
    static final String UNKNOWN = "unKnown";
    static final String LOCALHOST = "localhost";

    static final String COMMA = ",";


    /**
     * 获取有网关是 的真正客户端IP 测试过nginx可以获取
     * <pre>
     *
     * location /test/ {
     *     proxy_pass http://localhost:9002/;
     *     proxy_set_header Host $host;
     *     proxy_set_header X-Forwarded-Host $server_name;
     *     proxy_set_header X-Real-IP $remote_addr;
     *     proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
     * }
     * </pre>
     *
     * @param request request
     * @return ip
     */
    public static String getPoxyIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (null != ip && !UNKNOWN.equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (null != ip && !UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }


    public static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    /***
     * 增强版 - 普通使用getPoxyIp即可满足需求
     * 获取有网关是 的真正客户端IP 测试过nginx可以获取
     * @param request request
     * @return ip
     */
    public static String getPoxyIpEnhance(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            try {
                String ip = request.getHeader(header);
                if (ip != null && !ip.isEmpty() && !UNKNOWN.equalsIgnoreCase(ip)) {
                    return extractFirstIp(ip);
                }
            } catch (Exception e) {
                LOG.error("ip获取失败", e);
                return "127.0.0.1";
            }
        }
        // 如果无法通过 header 获取，则使用 getRemoteAddr
        String ip = request.getRemoteAddr();
        return LOCALHOST.equalsIgnoreCase(ip) || "127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)
                ? getLocalHostIp()
                : ip;
    }

    /**
     * 截取IP
     *
     * @param ip 逗号隔开的ip
     * @return 第一位IP
     */
    private static String extractFirstIp(String ip) {
        return ip.contains(COMMA) ? ip.split(COMMA)[0].trim() : ip;
    }

    /**
     * @return 获取本地 IP
     */
    public static String getLocalHostIp() {
        try {
            InetAddress inet = InetAddress.getLocalHost();
            return inet.getHostAddress();
        } catch (UnknownHostException e) {
            LOG.error("获取本地 IP 失败", e);
            return null;
        }
    }

    /**
     * 获取本地真正的IP地址，即获得有线或者无线WiFi地址。
     * 过滤虚拟机、蓝牙等地址
     *
     * @return java.lang.String
     * @author tn
     * @date 2020/4/21 23:44
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
            while (addresses.hasMoreElements()) {
                InetAddress ip = addresses.nextElement();
                // ipv4
                if (ip instanceof Inet4Address) {
                    return ip.getHostAddress();
                }
            }
            break;
        }

        return "127.0.0.1";
    }


    /**
     * 获得MAC地址
     *
     * @param ip ip
     * @return java.lang.String
     * @author tn
     * @date 2020/4/21 23:47
     */

    public static String getMacAddress(String ip) {
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
            LOG.error("获得MAC地址失败", e);
        }
        return macAddress;
    }

    /**
     * 域名转ip
     *
     * @param domainName 域名
     * @return String
     */
    public static String ipConvert(String domainName) {
        String ip;
        try {
            ip = InetAddress.getByName(domainName).getHostAddress();
        } catch (UnknownHostException e) {
            LOG.error("域名转ip失败", e);
            return domainName;
        }
        return ip;
    }

    /**
     * 获取客户端IP地址
     *
     * @return String tan/172.18.96.1
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
