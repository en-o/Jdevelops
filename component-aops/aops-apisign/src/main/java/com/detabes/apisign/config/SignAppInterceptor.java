package com.detabes.apisign.config;

import com.alibaba.fastjson.JSONObject;
import com.detabes.apisign.annotation.Signature;
import com.detabes.apisign.enums.SginEnum;
import com.detabes.encryption.core.SignMD5Util;
import com.detabes.encryption.core.SignShaUtil;
import com.detabes.exception.exception.BusinessException;
import com.detabes.http.core.HttpContextUtils;
import com.detabes.result.vo.ResultVO;
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
 * @author tn
 * @version 1
 * @ClassName AppInterceptor
 * @description 签名拦截器 使用签名注解需要注册该拦截器到 WebMvcConfigurer 中
 * @date 2020/6/9 14:56
 */
@Slf4j
public class SignAppInterceptor extends InterceptorRegistry implements HandlerInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(SignAppInterceptor.class);

    private static final String CONTENT_TYPE="text/json;charset=UTF-8";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            //签名验证注解
            Signature signAnt = ((HandlerMethod) handler).getMethodAnnotation(Signature.class);
            //验签
            if(signAnt!=null && !signCheck(request,signAnt.type())){
                response.setContentType(CONTENT_TYPE);
                response.getWriter().print(JSONObject.toJSONString(ResultVO.fail("签名不正确")));
                return false;
            }
            return true;
        }else{
            return true;
        }
    }

    /**
     *  签名验证
     * @param request req
     * @param enumm  SginEnum
     * @return
     */
    private boolean signCheck(HttpServletRequest request, SginEnum enumm) throws IOException {
        Object map= null;
        String paramsHeader = "";
        if(StringUtils.isNotEmpty(getHeaderSign(request))){
            paramsHeader = showParamsHeader(request);
        }else{
            map= showParams(request);
        }
        if(enumm== SginEnum.MD5){
            return SignMD5Util.check(map);
        }else if(enumm==SginEnum.SHA){
            return SignShaUtil.check(map);
        }else if(enumm==SginEnum.MD5HEADER){
            return SignMD5Util.checkHeader(request, paramsHeader);
        }
        return true;
    }



    /**
     * 获取请求信息 返回的加密集
     * @param request
     * @return
     */
    public static String showParamsHeader(HttpServletRequest request) throws IOException {
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
                }else{
                    map.put(paramName,paramValues);
                }

            }
            if(map.isEmpty()){
                //封装request
                String bodyString = HttpContextUtils.getBodyString(request);
                log.info("加密集："+ bodyString);
                return bodyString ;
            }else {
                String jsonString = JSONObject.toJSONString(map);
                log.info("加密集："+jsonString);
                return jsonString;
            }
        }catch (Exception e){
            throw new BusinessException("加密参数有误",e);
        }
    }
    /**
     * 获取请求信息 返回的加密集
     * @param request
     * @return
     */
    public static Object showParams(HttpServletRequest request) throws IOException {
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
            if(map.isEmpty()){
                //封装request
                String bodyString = HttpContextUtils.getBodyString(request);
                //获取json数据
                LinkedHashMap linkedHashMap = JSONObject.parseObject(bodyString, LinkedHashMap.class);
                log.info("加密集："+ JSONObject.toJSONString(linkedHashMap));
                return linkedHashMap ;
            }else {
                log.info("加密集："+ JSONObject.toJSONString(map));
                return map;
            }
        }catch (Exception e){
            throw new BusinessException("加密参数有误",e);
        }
    }

    /**
     * 从 request 获取token
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
            sign = request.getParameter(signName);
            return sign;
        }catch (Exception e){
            return null;
        }
    }

}
