package cn.tannn.jdevelops.webs.interceptor.util;

import com.google.gson.Gson;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求工具
 * @author tan
 */
public class RequestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(RequestUtil.class);


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
     * 获取请求Body
     *
     * @param request request
     * @return String
     */
    public static String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line ;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            LOG.debug("读取流失败", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.debug("关闭流失败", e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOG.debug("关闭流失败", e);
                }
            }
        }
        return sb.toString();
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
            LOG.debug("获取 @RequestBody 相关参数失败", e);
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
