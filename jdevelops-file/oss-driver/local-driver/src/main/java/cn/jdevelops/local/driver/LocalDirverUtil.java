package cn.jdevelops.local.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Enumeration;

/**
 * 本地使用的util
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-05-04 13:52
 */
@SuppressWarnings("all")
public class LocalDirverUtil {

    private static final Logger LOG = LoggerFactory.getLogger(LocalOperate.class);


    /**
     * springboot 自带MD5加密
     * @param str 待加密字符串
     * @return 16进制加密字符串
     */

    public static String encrypt2MD5(String str) {
        String md5 = "images";
        md5 = DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
        return md5;
    }


    /**
     * 获取本地真正的IP地址，即获得有线或者无线WiFi地址。(过滤虚拟机、蓝牙等地址)
     *
     * @return java.lang.String
     * @author tn
     * @date 2020/4/21 23:44
     */

    public static String getRealIp() {
        try {
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
                    if (ip != null) {
                        // ipv4
                        if (ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
                break;
            }
        } catch (SocketException e) {
            LOG.error("获取主机ip地址时出错"
                    + e.getMessage());
        }
        return "127.0.0.1";
    }
}
