package cn.jdevelops.jwtweb.interceptor;

import cn.jdevelops.exception.result.ExceptionResultWrap;
import cn.jdevelops.jwt.annotation.ApiMapping;
import cn.jdevelops.jwt.annotation.NotRefreshToken;
import cn.jdevelops.jwt.constant.JwtConstant;
import cn.jdevelops.jwt.util.JwtUtil;
import cn.jdevelops.spi.ExtensionLoader;
import com.alibaba.fastjson.JSON;
import cn.jdevelops.enums.result.ResultCodeEnum;
import cn.jdevelops.jwtweb.server.CheckTokenInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author tan
 * @date 2020/4/18 22:12
 */
@Slf4j
public class WebApiInterceptor implements HandlerInterceptor {
    private CheckTokenInterceptor checkTokenInterceptor;

    public WebApiInterceptor() {
        getCheckTokenInterceptor();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Logger logger = LoggerFactory.getLogger(handlerMethod.getBeanType());
        if (method.isAnnotationPresent(ApiMapping.class)) {
            ApiMapping annotation = method.getAnnotation(ApiMapping.class);
            if (annotation.checkToken()) {
                return check_refresh_token(request, response, handler, method, logger);
            }else{
                return true;
            }
        }else{
            return check_refresh_token(request, response, handler, method, logger);
        }
    }


    /**
     *  验证并刷新token缓存
     *
     * @param request request
     * @param response response
     * @param handler handler
     * @param method method
     * @param logger logger
     * @return boolean
     * @throws IOException
     */
    private boolean check_refresh_token(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Object handler, Method method,
                                        Logger logger) throws IOException {
        boolean check = check(request, response, handler, logger);
        if(check){
            refreshToken(request, method);
        }
        return check;
    }


    /**
     * 验证token
     * @param request request
     * @param response response
     * @param handler handler
     * @param logger logger
     * @return check
     * @throws IOException IOException
     */
    private boolean check(HttpServletRequest request, HttpServletResponse response, Object handler, Logger logger) throws IOException {
        String token = getToken(request);
        boolean flag = checkTokenInterceptor.checkToken(token);
        logger.info("需要验证token,校验结果：{},token:{}", flag,token);
        if (!flag) {
            response.setHeader("content-type", "application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(ExceptionResultWrap.error(ResultCodeEnum.TOKEN_ERROR.getCode(), "无效的token")));
            return false;
        }
        MDC.put(JwtConstant.TOKEN, token);
        return true;
    }

    /**
     * 获取token
     * @param request request
     * @return String
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(JwtConstant.TOKEN);
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        token = request.getParameter(JwtConstant.TOKEN);
        return token;
    }


    /**
     * 刷新缓存
     * @param request request
     * @param method method
     */
    private void refreshToken(HttpServletRequest request, Method method) {
        // token缓存刷新
        try {
            //  此注解表示不刷新缓存
            if(!method.isAnnotationPresent(NotRefreshToken.class)){
                // 每次接口进来都要属性 token缓存。刷新方式请自主实现
                checkTokenInterceptor.refreshToken(getUserNoByToken(request));
            }
        }catch (Exception e){
            log.warn("token缓存刷新失败",e);
        }
    }

    /**
     * 获取用户的唯一编码
     * @param request request
     * @return String
     */
    private String getUserNoByToken(HttpServletRequest request) {
        String token = getToken(request);
        return JwtUtil.getSubject(token);
    }

    private  void getCheckTokenInterceptor (){
        checkTokenInterceptor = ExtensionLoader.getExtensionLoader(CheckTokenInterceptor.class).getJoin("defaultInterceptor");
    }
}
