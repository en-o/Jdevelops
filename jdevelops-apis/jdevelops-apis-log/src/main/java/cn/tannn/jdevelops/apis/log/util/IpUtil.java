package cn.tannn.jdevelops.apis.log.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;


/**
 * @author 谭宁
 */
public class IpUtil {

    private static final Logger LOG = LoggerFactory.getLogger(IpUtil.class);
    static final String UNKNOWN = "unKnown";
    static final String LOCALHOST = "localhost";

    static final String COMMA = ",";


    /***
     * 获取请求头中的IP
     * @param request request
     * @return ip
     */
    public static String httpRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_FORWARDED");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_VIA");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 本机访问
        if (LOCALHOST.equalsIgnoreCase(ip)
                || "127.0.0.1".equalsIgnoreCase(ip)
                || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)) {
            // 根据网卡取本机配置的IP
            InetAddress inet;
            try {
                inet = InetAddress.getLocalHost();
                ip = inet.getHostAddress();
            } catch (UnknownHostException e) {
                LOG.error("获取Ip失败", e);
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (null != ip && ip.indexOf(COMMA) > 15) {
            ip = ip.substring(0, ip.indexOf(COMMA));
        }
        return ip;
    }

    /**
     * 验证是否可以打印入参
     * <p> 真 不需要打印
     * <p> 假 需要打印
     * @param interceptApi 需要不打印的入参的api
     * @param api 当前请求的接口地址
     * @return Boolean
     */
    public static Boolean printParams(Set<String> interceptApi
            , String api){
        if(null != interceptApi && !interceptApi.isEmpty() && api != null ) {
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            return interceptApi.stream()
                    .anyMatch(e ->antPathMatcher.match(e,api));
        }
        return false;
    }
}
