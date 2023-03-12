package cn.jdevelops.sboot.swagger.core.util;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static cn.jdevelops.sboot.swagger.core.constant.PublicConstant.COMMA;
import static cn.jdevelops.sboot.swagger.core.constant.PublicConstant.SPLITOR;

/**
 * swagger 的一些公共方法
 * @author tnnn
 * @version V1.0
 * @date 2022-07-25 10:13
 */
public class SwaggerUtil {

    private static final Logger LOG = LoggerFactory.getLogger(SwaggerUtil.class);

    /**
     * 解析  多个以字符分割的basePackage
     */
    public static String[] basePackages(final String basePackage)     {
        // 循环判断匹配
        String basePackages = basePackage.replaceAll(COMMA, SPLITOR);
        return basePackages.split(SPLITOR);
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
                    // ipv4
                    if (ip instanceof Inet4Address) {
                        return ip.getHostAddress();
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
