package cn.tannn.jdevelops.jwt.standalone.interceptor;


import cn.tannn.jdevelops.annotations.web.authentication.ApiMapping;
import cn.tannn.jdevelops.exception.aspect.ExceptionAspect;
import cn.tannn.jdevelops.jwt.standalone.interceptor.check.CheckSpecialPath;
import cn.tannn.jdevelops.jwt.standalone.interceptor.check.CheckTokenService;
import cn.tannn.jdevelops.jwt.standalone.service.CheckTokenInterceptor;
import cn.tannn.jdevelops.jwt.standalone.exception.TokenCode;
import cn.tannn.jdevelops.result.exception.ExceptionResultWrap;
import cn.tannn.jdevelops.spi.ExtensionLoader;
import cn.tannn.jdevelops.utils.jwt.config.JwtConfig;
import com.alibaba.fastjson2.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;


/**
 * 拦截器拦截 接口验证
 *
 * @author tan
 * @date 2020/4/18 22:12
 */
public class JwtWebApiInterceptor implements HandlerInterceptor {

    private static final Logger logger_global = LoggerFactory.getLogger(ExceptionAspect.class);

    private CheckTokenInterceptor checkTokenInterceptor;

    private final JwtConfig jwtConfig;

    public JwtWebApiInterceptor(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        getCheckTokenInterceptor();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof ResourceHttpRequestHandler && jwtConfig.getOss().isEnable()) {
            ResourceHttpRequestHandler resourceHandler = (ResourceHttpRequestHandler) handler;
            String servletPath = request.getServletPath();
            logger_global.debug("====> servlet path {}, resourceHandler str {}", servletPath, resourceHandler);
            Integer status =  new CheckSpecialPath(resourceHandler, servletPath, request, response,jwtConfig, checkTokenInterceptor)
                    .checkOssToken();
            if (status != null) {
                return status == 1;
            }
        }
        // 放行 非HandlerMethod 请求
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 获取处理器方法所属的类
        Class<?> controllerClass = handlerMethod.getBeanType();

        Method method = handlerMethod.getMethod();
        Logger logger = LoggerFactory.getLogger(handlerMethod.getBeanType());

        CheckTokenService checkToken = new CheckTokenService(request
                , response
                , method
                , logger
                , jwtConfig
                , controllerClass
                , checkTokenInterceptor);

        if (method.isAnnotationPresent(ApiMapping.class)) {
            ApiMapping annotation = method.getAnnotation(ApiMapping.class);
            if (annotation.checkToken()) {
                return checkToken.check_refresh_token();
            } else {
                return true;
            }
        } else {
            return checkToken.check_refresh_token();
        }
    }


// =================== 检查接口的使用范围是否能跟token中的返回对应上 ==================================


    private void getCheckTokenInterceptor() {
        checkTokenInterceptor = ExtensionLoader.getExtensionLoader(CheckTokenInterceptor.class).getJoin("defaultInterceptor");
    }



    /**
     * 给 response 添加错误信息
     */
    public static void extractedErrorResponse(HttpServletResponse response) throws IOException {
        response.setHeader("content-type", "application/json;charset=UTF-8");
        response.getOutputStream().write(JSON.toJSONString(ExceptionResultWrap
                .result(TokenCode.TOKEN_ERROR.getCode(), TokenCode.TOKEN_ERROR.getMessage())).getBytes(StandardCharsets.UTF_8));
    }


}
