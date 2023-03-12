package cn.jdevelops.sboot.swagger.core.util;



import java.net.InetAddress;

import static cn.jdevelops.sboot.swagger.core.constant.PublicConstant.COMMA;
import static cn.jdevelops.sboot.swagger.core.constant.PublicConstant.SPLITOR;

/**
 * swagger 的一些公共方法
 * @author tnnn
 * @version V1.0
 * @date 2022-07-25 10:13
 */
public class SwaggerUtil {



    /**
     * 解析  多个以字符分割的basePackage
     */
    public static String[] basePackages(final String basePackage)     {
        // 循环判断匹配
        String basePackages = basePackage.replaceAll(COMMA, SPLITOR);
        return basePackages.split(SPLITOR);
    }


    /**
     * 获取本地IP
     * @return  ip
     */
    public static String localIP(){
        String address = "127.0.0.1";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            address = inetAddress.getHostAddress();
        }catch (Exception ignored){
        }
        return address;
    }
}
