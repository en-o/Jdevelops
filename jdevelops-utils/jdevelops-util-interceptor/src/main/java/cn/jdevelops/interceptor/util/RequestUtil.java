package cn.jdevelops.interceptor.util;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求工具
 * @author tan
 */
public class RequestUtil {


    /**
     * 获取浏览器参数
     * @param request request
     * @return String
     */
    public static String requestParams(HttpServletRequest request){
        String queryString = queryString(request);
        return StrUtil.isNull(queryString)?bodyString(request):queryString;
    }

    /**
     * 将浏览器参数，对GET请求进行中文乱码处理
     * @param request request
     * @return queryString
     */
    public static String queryString(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<>(10);
        Enumeration<?> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            if (key != null) {
                if (key instanceof String) {
                    String value = request.getParameter(key.toString());
                    paramMap.put(key.toString(), value);
                }
            }
        }
        return paramMap.isEmpty()?"":new Gson().toJson(paramMap);
    }


    /**
     * 获取 @RequestBody 相关参数
     *
     * @param request request
     * @return String
     */
    public static String bodyString(HttpServletRequest request) {
        StringBuilder param = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                param.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return StrUtil.isNull(param)?"":param.toString();
    }


    /**
     * 判断是否是multipart/form-data请求
     *
     * @param request request
     * @return true 是form-data
     */
    public static boolean isMultipartContent(HttpServletRequest request) {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        //获取Content-Type
        String contentType = request.getContentType();
        return (contentType != null) && (contentType.toLowerCase().startsWith("MULTIPART/".toLowerCase()));
    }

}