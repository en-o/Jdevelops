package cn.tannn.jdevelops.jwt.standalone.interceptor.check;


import cn.tannn.jdevelops.annotations.web.authentication.NotRefreshToken;
import cn.tannn.jdevelops.jwt.standalone.pojo.CheckToken;
import cn.tannn.jdevelops.jwt.standalone.service.CheckTokenInterceptor;
import cn.tannn.jdevelops.jwt.standalone.util.JwtWebUtil;
import cn.tannn.jdevelops.utils.jwt.config.JwtConfig;
import cn.tannn.jdevelops.utils.jwt.constant.JwtConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.reflect.Method;

import static cn.tannn.jdevelops.jwt.standalone.interceptor.JwtWebApiInterceptor.extractedErrorResponse;


/**
 * token 检查
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/26 下午2:47
 */
public class CheckTokenService {

    private static final Logger logger_global = LoggerFactory.getLogger(CheckTokenService.class);

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final Method method;
    private final Logger logger;
    private final JwtConfig jwtConfig;
    private final CheckTokenInterceptor checkTokenInterceptor;
    /**
     * 处理器方法所属的类
     */
    Class<?> controllerClass;

    public CheckTokenService(HttpServletRequest request
            , HttpServletResponse response
            , Method method
            , Logger logger
            , JwtConfig jwtConfig
            , Class<?> controllerClass
            , CheckTokenInterceptor checkTokenInterceptor) {
        this.request = request;
        this.response = response;
        this.method = method;
        this.logger = logger;
        this.jwtConfig = jwtConfig;
        this.controllerClass = controllerClass;
        this.checkTokenInterceptor = checkTokenInterceptor;
    }

    /**
     * 验证并刷新token缓存
     */
    public boolean check_refresh_token() throws Exception {
        CheckToken check = check();
        if (Boolean.TRUE.equals(check.getCheck())) {
            refreshToken(check.getToken(), method);
        }
        return check.getCheck();
    }


    /**
     * 刷新缓存 - redis中才有用
     *
     * @param token  token
     * @param method method
     */
    public void refreshToken(String token, Method method) {
        // token缓存刷新
        try {

            // 全局设置刷新状态 false: 不刷新
            if (Boolean.TRUE.equals(jwtConfig.getCallRefreshToken())
                && (!method.isAnnotationPresent(NotRefreshToken.class))) {
                // 每次接口进来都要属性 token缓存。刷新方式请自主实现
                checkTokenInterceptor.refreshToken(token);
            }
        } catch (Exception e) {
            logger_global.warn("token缓存刷新失败", e);
        }
    }

    /**
     * 验证token
     */
    private CheckToken check() throws Exception {
        String token = JwtWebUtil.getToken(request, jwtConfig.getCookie());
        // 验证token
        boolean flag = checkTokenInterceptor.checkToken(token);
        logger.info("需要验证token,url:{}, 校验结果：{},token:{}", request.getRequestURI(), flag, token);
        if (!flag) {
            extractedErrorResponse(response);
            return new CheckToken(false, token);
        }
        /* 日志用的 - %X{token} */
        MDC.put(JwtConstant.TOKEN, token);

        CheckPlatformService checkPlatform = new CheckPlatformService(token, method, controllerClass);
        CheckUserService checkUser = new CheckUserService(checkTokenInterceptor);

        // 验证接口是否允许被调用
        if (Boolean.TRUE.equals(jwtConfig.getVerifyPlatform())) {
            checkPlatform.checkApiPlatform(request.getServletPath());
        }
        // 验证用户状态
        checkUser.checkUserStatus(token);
        // 验证用户接口权限
        if (Boolean.TRUE.equals(jwtConfig.getVerifyPermission())) {
            checkUser.checkUserPermission(token, method);
        }
        return new CheckToken(true, token);
    }





}
