package cn.tannn.jdevelops.sign;

import cn.tannn.jdevelops.result.exception.ExceptionResultWrap;
import cn.tannn.jdevelops.sign.annotation.Signature;
import cn.tannn.jdevelops.sign.config.ApiSignConfig;
import cn.tannn.jdevelops.sign.enums.SginEnum;
import cn.tannn.jdevelops.sign.enums.SignExceptionCode;
import cn.tannn.jdevelops.sign.exception.SignException;
import cn.tannn.jdevelops.sign.util.SignMD5Util;
import cn.tannn.jdevelops.sign.util.SignShaUtil;
import cn.tannn.jdevelops.webs.interceptor.ApiBeforeInterceptor;
import cn.tannn.jdevelops.webs.interceptor.util.RequestUtil;
import cn.tannn.jdevelops.webs.interceptor.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.HandlerMethod;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.alibaba.fastjson2.JSON.parseObject;
import static com.alibaba.fastjson2.JSON.toJSONString;

/**
 * 签名拦截器 使用签名注解需要注册该拦截器到 WebMvcConfigurer 中
 *
 * @author tn
 * @version 1
 * @date 2020/6/9 14:56
 */
@Order(2)
public class SignAppInterceptor implements ApiBeforeInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(SignAppInterceptor.class);

    private  final ApiSignConfig apiSignConfig;

    public SignAppInterceptor(ApiSignConfig apiSignConfig) {
        this.apiSignConfig = apiSignConfig;
    }

    @Override
    public boolean before(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            //签名验证注解
            Signature signAnt = ((HandlerMethod) handler).getMethodAnnotation(Signature.class);
            //验签
            if (signAnt != null && !signCheck(request, signAnt.type(), apiSignConfig.getSalt())) {
                response.setHeader("content-type", "application/json;charset=UTF-8");
                response.getOutputStream().write(toJSONString(ExceptionResultWrap
                        .result(SignExceptionCode.API_SIGN_ERROR)).getBytes(StandardCharsets.UTF_8));
                return false;
            }
        }
        return true;
    }

    /**
     * 签名验证
     *
     * @param request req
     * @param enums   SignEnum
     * @param salt    盐
     * @return boolean
     */
    private boolean signCheck(HttpServletRequest request, SginEnum enums, String salt) {
        Object map = null;
        String paramsHeader = "";
        if (StrUtil.isNotBlank(getHeaderSign(request))) {
            paramsHeader = showParamsHeader(request);
        } else {
            map = showParams(request);
        }
        if(LOG.isDebugEnabled()){
            String mapstr = "";
            if(map!=null){
                try {
                    mapstr = JSON.toJSONString(map);
                }catch (Exception ignored){}
            }
            LOG.debug("加密集 String:{} ，map:{}", paramsHeader, mapstr);
        }
        if (enums == SginEnum.MD5) {
            return SignMD5Util.check(map, salt);
        } else if (enums == SginEnum.SHA) {
            return SignShaUtil.check(map);
        } else if (enums == SginEnum.MD5HEADER) {
            return SignMD5Util.checkHeader(request, paramsHeader, salt);
        }
        return true;
    }


    /**
     * 获取请求信息 返回的加密集
     *
     * @param request request
     * @return String
     */
    public static String showParamsHeader(HttpServletRequest request) {
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
                return JSON.parse(RequestUtil.getBodyString(request)).toString();
            } else {
                return toJSONString(map);
            }
        } catch (Exception e) {
            throw new SignException("加密参数有误", e);
        }
    }

    /**
     * 获取请求信息 返回的加密集
     */
    public static Object showParams(HttpServletRequest request) {
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
                }
            }
            if (map.isEmpty()) {
                //封装request
                String bodyString = RequestUtil.getBodyString(request);
                //获取json数据
                return parseObject(bodyString, LinkedHashMap.class);
            } else {
                return map;
            }
        } catch (Exception e) {
            throw new SignException("加密参数有误", e);
        }
    }

    /**
     * 从 request 获取 sign
     *
     * @param request request
     * @return sign
     */
    public static String getHeaderSign(HttpServletRequest request) {
        try {
            final String signName = "sign";
            String sign = request.getHeader(signName);
            if (StrUtil.isNotBlank(sign)) {
                return sign;
            }
        } catch (Exception e) {
            LOG.warn("消息头中没有sign");
        }
        return null;
    }


}
