package cn.jdevelops.api.sign.core;

import cn.jdevelops.aops.AopContextUtil;
import cn.jdevelops.aops.StringUtil;
import cn.jdevelops.api.sign.config.ApiSignConfig;
import cn.jdevelops.api.sign.exception.SignException;
import cn.jdevelops.aops.HttpUtil;
import cn.jdevelops.api.sign.annotation.Signature;
import cn.jdevelops.api.sign.enums.SginEnum;
import cn.jdevelops.encryption.core.SignMD5Util;
import cn.jdevelops.encryption.core.SignShaUtil;
import cn.jdevelops.api.result.custom.ExceptionResultWrap;
import cn.jdevelops.util.interceptor.api.ApiBeforeInterceptor;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static cn.jdevelops.aops.CommonConstant.CONTENT_TYPE;
import static cn.jdevelops.api.sign.enums.SginExceptionCodeEnum.API_SIGN_ERROR;
import static com.alibaba.fastjson.JSON.*;

/**
 * 签名拦截器 使用签名注解需要注册该拦截器到 WebMvcConfigurer 中
 *
 * @author tn
 * @version 1
 * @date 2020/6/9 14:56
 */
@Component
@Order(2)
@Slf4j
public class SignAppInterceptor implements ApiBeforeInterceptor {
    @Override
    public boolean before(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            //签名验证注解
            Signature signAnt = ((HandlerMethod) handler).getMethodAnnotation(Signature.class);
            ApiSignConfig apiSignBean = AopContextUtil.getBean(ApiSignConfig.class);
            //验签
            if (signAnt != null && !signCheck(request, signAnt.type(), apiSignBean.getSalt())) {
                response.setContentType(CONTENT_TYPE);
                response.getWriter().print(toJSONString(ExceptionResultWrap
                        .result(API_SIGN_ERROR.getCode(), API_SIGN_ERROR.getMessage())));
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
        if (StringUtil.isNotBlank(getHeaderSign(request))) {
            paramsHeader = showParamsHeader(request);
        } else {
            map = showParams(request);
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
                    if (paramValue.length() != 0) {
                        map.put(paramName, paramValue);
                    }
                } else {
                    map.put(paramName, paramValues);
                }

            }
            if (map.isEmpty()) {
                //封装request
                String bodyString = parse(HttpUtil.getBodyString(request), Feature.OrderedField).toString();
                log.info("加密集：" + bodyString);
                return bodyString;
            } else {
                String jsonString = toJSONString(map);
                log.info("加密集：" + jsonString);
                return jsonString;
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
                    if (paramValue.length() != 0) {
                        map.put(paramName, paramValue);
                    }
                }
            }
            if (map.isEmpty()) {
                //封装request
                String bodyString = HttpUtil.getBodyString(request);
                //获取json数据
                LinkedHashMap<String, Object> linkedHashMap = parseObject(bodyString, LinkedHashMap.class);
                log.info("加密集：" + toJSONString(linkedHashMap));
                return linkedHashMap;
            } else {
                log.info("加密集：" + toJSONString(map));
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
            if (StringUtil.isNotBlank(sign)) {
                return sign;
            }
        } catch (Exception e) {
            log.warn("消息头中没有sign");
        }
        return null;
    }


}
