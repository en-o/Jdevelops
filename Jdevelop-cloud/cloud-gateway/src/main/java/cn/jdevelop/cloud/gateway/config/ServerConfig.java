package cn.jdevelop.cloud.gateway.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 控制台输出 Swagger 接口文档地址
 * @author tn
 * @version 1
 * @date 2020/6/18 16:24
 */

@Slf4j
public class ServerConfig implements ApplicationListener<WebServerInitializedEvent> {

    private int serverPort;

    @Value("${server.servlet.context-path:/}")
    private String serverName;


    public int getPort() {
        return this.serverPort;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        try {
            this.serverPort = event.getWebServer().getPort();
            if("/".equals(serverName)){
                serverName = "";
            }
            log.info("\n----------------------------------------------------------\n\t" +
                    " swagger 启动. Access URLs:\n\t" +
                    "swagger 启动成功！接口文档地址-HTML: http://"+getRealIp()+":"+serverPort+serverName+"/doc.html" + "\n\t" +
                    "----------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     *
     *获取本地真正的IP地址，即获得有线或者无线WiFi地址。
     *
     * @author tn
     * @date  2020/4/21 23:44
     * @description 过滤虚拟机、蓝牙等地址
     * @return java.lang.String
     */

    private static String getRealIp() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
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
            log.error("获取主机ip地址时出错"
                    + e.getMessage());
        }
        return "127.0.0.1";
    }




}

