package cn.tannn.jdevelops.idempotent.util;

import cn.tannn.jdevelops.idempotent.exception.IdempotentException;
import com.alibaba.fastjson2.JSON;
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
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 参数
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-11-17 15:01
 */
public class ParamUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ParamUtil.class);

    /**
     * 获取接口参数
     *
     * @param request request
     * @return String
     */
    public static String getRequestParam(HttpServletRequest request) {
        try {
            Map<String, Object> map = new LinkedHashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String[] paramValues = request.getParameterValues(paramName);
                if (paramValues.length == 1) {
                    String paramValue = paramValues[0];
                    if (!paramValue.isEmpty()) {
                        map.put(paramName, paramValue);
                    }
                } else {
                    map.put(paramName, paramValues);
                }

            }
            if (map.isEmpty()) {
                //封装request
                String requsetStr = getBodyString(request);
                if ("".equals(requsetStr)) {
                    return requsetStr;
                }
                String bodyString = JSON.parse(requsetStr).toString();
                LOG.debug("参数string：" + requsetStr);
                return bodyString;
            } else {
                String jsonString = JSON.toJSONString(map);
                LOG.debug("参数string：" + jsonString);
                return jsonString;
            }
        } catch (Exception e) {
            throw new IdempotentException("接口参数获取失败", e);
        }
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
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            LOG.debug("获取请求Body失败", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.debug("获取请求Body关流失败", e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOG.debug("获取请求Body关流失败", e);
                }
            }
        }
        return sb.toString();
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
