package cn.jdevelops.util.http;

import javax.servlet.http.HttpServletRequest;

/**
 * request工具类
 * @author tn
 * @version 1
 * @date 2021/1/12 22:40
 */
public class RequestUtil {

    /**
     * 从 request 获取参数值
     * @param request request
     * @param param 指定key
     * @return String
     */
    public static String getRequestParamValue(HttpServletRequest request,String param) {
        String token = request.getHeader(param);
        if (null!=token&&token.length()>0) {
            return token;
        }
        token = request.getParameter(param);
        return token;
    }

}
