package cn.tannn.jdevelops.knife4j.core.util;


import cn.tannn.jdevelops.knife4j.core.entity.BuildSecuritySchemes;
import cn.tannn.jdevelops.knife4j.core.entity.SwaggerSecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;


/**
 * swagger 的一些公共方法
 * @author tnnn
 * @version V1.0
 * @date 2022-07-25 10:13
 */
public class SwaggerUtil {

    private static final Logger LOG = LoggerFactory.getLogger(SwaggerUtil.class);

    /**
     * 解析basePackage
     */
    public static String[] basePackages(final List<String> basePackage)     {
        return basePackage.toArray(new String[basePackage.size()]);
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
            LOG.error("获取主机ip地址时出错{}",e.getMessage());
        }
        return "127.0.0.1";
    }


    /**
     *
     * OpenAPI 规范中支持的安全方案是
     * <pre>
     *  <a href="http://www.ballcat.cn/guide/feature/openapi.html#%E5%AE%89%E5%85%A8%E6%96%B9%E6%A1%88">参考</a>
     * HTTP 身份验证
     * API key （作为 Header 或 查询参数）
     * OAuth2 的通用流程（implicit, password, application and access code），如RFC6749
     * OpenID Connect Discovery
     * 在 java 中的抽象类型对应 io.swagger.v3.oas.models.security.SecurityScheme
     * </pre>
     * @param swaggerSecuritySchemes SwaggerProperties.getSwaggerSecuritySchemes
     */
    public static BuildSecuritySchemes buildSecuritySchemes(List<SwaggerSecurityScheme> swaggerSecuritySchemes) {
        List<SecurityRequirement> securityItem = new ArrayList<>();
        Map<String, SecurityScheme> securitySchemes = new HashMap<>(10);
        swaggerSecuritySchemes.forEach(swaggerSecurityScheme -> {
            if(Boolean.TRUE.equals(swaggerSecurityScheme.getSecurity())){
                securitySchemes.put(swaggerSecurityScheme.getScheme().getType().name(), swaggerSecurityScheme.getScheme());
                securityItem.add(new SecurityRequirement().addList(swaggerSecurityScheme.getScheme().getType().name()));
            }
        });
        return new BuildSecuritySchemes(securityItem,securitySchemes);
    }
}
