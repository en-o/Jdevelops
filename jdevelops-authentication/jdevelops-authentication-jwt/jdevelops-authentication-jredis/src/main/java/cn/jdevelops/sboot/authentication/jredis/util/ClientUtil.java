package cn.jdevelops.sboot.authentication.jredis.util;


import javax.servlet.http.HttpServletRequest;



/**
 *  getHeader("user-agent") 客户端信息记录
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-28 14:21
 */
public class ClientUtil {

    /**
     * 获取（浏览器）请求头中的客户端
     * @param request request
     * @return 客户端
     */
    public static String getRequestHeader(HttpServletRequest request){
        String info= request.getHeader("user-agent");
        // Windows pc端登陆
        if(info.contains("Windows")){
            return "Windows";
        }
        // Mac pc端登陆
        if(info.contains("Macintosh")){
            return "Macintosh";
        }
        // Android移动客户端
        if(info.contains("Android")) {
            return "Android";
        }
        // iPhone移动客户端
        if(info.contains("iPhone")) {
            return "iPhone";
        }
        // iPad客户端
        if(info.contains("iPad")) {
            return "iPad";
        }
        return "unknown";
    }
}
