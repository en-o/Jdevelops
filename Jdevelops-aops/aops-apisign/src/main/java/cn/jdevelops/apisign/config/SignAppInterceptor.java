package cn.jdevelops.apisign.config;

import cn.jdevelops.apisign.bean.ApiSignBean;
import cn.jdevelops.enums.result.ResultCodeEnum;
import cn.jdevelops.exception.result.ExceptionResultWrap;
import cn.jdevelops.exception.utils.SpringBeanUtils;
import cn.jdevelops.spring.core.context.ContextUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.jdevelops.apisign.annotation.Signature;
import cn.jdevelops.apisign.enums.SginEnum;
import cn.jdevelops.encryption.core.SignMD5Util;
import cn.jdevelops.encryption.core.SignShaUtil;
import cn.jdevelops.exception.exception.BusinessException;
import cn.jdevelops.http.core.HttpContextUtils;
import cn.jdevelops.result.result.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 签名拦截器 使用签名注解需要注册该拦截器到 WebMvcConfigurer 中
 *
 * @author tn
 * @version 1
 * @date 2020/6/9 14:56
 */
@Slf4j
public class SignAppInterceptor extends InterceptorRegistry implements HandlerInterceptor {

    private static final String CONTENT_TYPE = "text/json;charset=UTF-8";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            //签名验证注解
            Signature signAnt = ((HandlerMethod) handler).getMethodAnnotation(Signature.class);
            ApiSignBean apiSignBean = SpringBeanUtils.getInstance().getBean(ApiSignBean.class);
            //验签
            if (signAnt != null && !signCheck(request, signAnt.type(),apiSignBean.getSalt())) {
                response.setContentType(CONTENT_TYPE);
                response.getWriter().print(JSONObject.toJSONString(ExceptionResultWrap.error(ResultCodeEnum.API_SIGN_ERROR)));
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * 签名验证
     *
     * @param request req
     * @param enumm   SginEnum
     * @param salt 盐
     * @return boolean
     */
    private boolean signCheck(HttpServletRequest request, SginEnum enumm, String salt) throws IOException {
        Object map = null;
        String paramsHeader = "";
        if (StringUtils.isNotEmpty(getHeaderSign(request))) {
            paramsHeader = showParamsHeader(request);
        } else {
            map = showParams(request);
        }
        if (enumm == SginEnum.MD5) {
            return SignMD5Util.check(map, salt);
        } else if (enumm == SginEnum.SHA) {
            return SignShaUtil.check(map);
        } else if (enumm == SginEnum.MD5HEADER) {
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
            Map map = new LinkedHashMap();
            Enumeration paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();
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
                String bodyString = HttpContextUtils.getBodyString(request);
                log.info("加密集：" + bodyString);
                return bodyString;
            } else {
                String jsonString = JSONObject.toJSONString(map);
                log.info("加密集：" + jsonString);
                return jsonString;
            }
        } catch (Exception e) {
            throw new BusinessException("加密参数有误", e);
        }
    }

    /**
     * 获取请求信息 返回的加密集
     */
    public static Object showParams(HttpServletRequest request) {
        try {
            Map map = new LinkedHashMap();
            Enumeration paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();
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
                String bodyString = HttpContextUtils.getBodyString(request);
                //获取json数据
                LinkedHashMap linkedHashMap = JSON.parseObject(bodyString, LinkedHashMap.class);
                log.info("加密集：" + JSON.toJSONString(linkedHashMap));
                return linkedHashMap;
            } else {
                log.info("加密集：" + JSON.toJSONString(map));
                return map;
            }
        } catch (Exception e) {
            throw new BusinessException("加密参数有误", e);
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
            if (StringUtils.isNotBlank(sign)) {
                return sign;
            }
        } catch (Exception e) {
            log.warn("消息头中没有sign");
        }
        return null;
    }

}
