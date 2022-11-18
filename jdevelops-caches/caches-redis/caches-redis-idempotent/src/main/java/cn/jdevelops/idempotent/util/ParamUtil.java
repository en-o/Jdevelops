package cn.jdevelops.idempotent.util;

import cn.jdevelops.idempotent.exception.IdempotentException;
import com.alibaba.fastjson.parser.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.alibaba.fastjson.JSON.*;

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
     * @param request request
     * @return String
     */
    public static String getRequestParam(HttpServletRequest request) {
        try {
            Map<String,Object> map = new LinkedHashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName =  paramNames.nextElement();
                String[] paramValues = request.getParameterValues(paramName);
                if (paramValues.length == 1) {
                    String paramValue = paramValues[0];
                    if (paramValue.length() != 0) {
                        map.put(paramName, paramValue);
                    }
                } else {
                    map.put(paramName, paramValues);
                }

            }
            if (map.isEmpty()) {
                //封装request
                String requsetStr = getBodyString(request);
                if("".equals(requsetStr)){
                    return requsetStr;
                }
                String bodyString = parse(requsetStr, Feature.OrderedField).toString();
                LOG.debug("参数string：" + requsetStr);
                return bodyString;
            } else {
                String jsonString = toJSONString(map);
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
            String line ;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
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
