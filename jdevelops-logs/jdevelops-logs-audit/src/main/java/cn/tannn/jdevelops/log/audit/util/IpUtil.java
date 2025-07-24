package cn.tannn.jdevelops.log.audit.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/24 10:13
 */
public class IpUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpUtil.class);
    private static final Logger LOG = LoggerFactory.getLogger(IpUtil.class);
    static final String UNKNOWN = "unKnown";
    static final String LOCALHOST = "localhost";
    static final String COMMA = ",";

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
        try {
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
        }catch (Exception e){
            LOG.error("audit log ip获取失败:{}", e.getMessage());
            return "127.0.0.1";
        }
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


}
