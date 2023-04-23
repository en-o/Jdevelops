package cn.jdevelops.sboot.authentication.jwt.interceptor;

import cn.jdevelops.api.result.custom.ExceptionResultWrap;
import cn.jdevelops.api.result.emums.TokenExceptionCodeEnum;
import cn.jdevelops.sboot.authentication.jwt.annotation.ApiMapping;
import cn.jdevelops.sboot.authentication.jwt.annotation.NotRefreshToken;
import cn.jdevelops.sboot.authentication.jwt.server.CheckTokenInterceptor;
import cn.jdevelops.sboot.authentication.jwt.util.JwtWebUtil;
import cn.jdevelops.sboot.authentication.jwt.vo.CheckVO;
import cn.jdevelops.spi.ExtensionLoader;
import cn.jdevelops.util.jwt.config.JwtConfig;
import cn.jdevelops.util.jwt.constant.JwtConstant;
import cn.jdevelops.util.jwt.core.JwtService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
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

    private final JwtConfig jwtConfig;

    public WebApiInterceptor(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
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
     * @throws IOException Exception
     */
    private boolean check_refresh_token(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Object handler, Method method,
                                        Logger logger) throws Exception {
        CheckVO check = check(request, response, handler, logger);
        if(check.getCheck()){
            refreshToken(check.getToken(), method);
        }
        return check.getCheck();
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
    private CheckVO check(HttpServletRequest request, HttpServletResponse response, Object handler, Logger logger) throws Exception {
        String  token = JwtWebUtil.getToken(request, jwtConfig.getCookie());
        // 验证token
        boolean flag = checkTokenInterceptor.checkToken(token);
        logger.info("需要验证token,校验结果：{},token:{}", flag,token);
        if (!flag) {
            response.setHeader("content-type", "application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(ExceptionResultWrap
                    .result(TokenExceptionCodeEnum.TOKEN_ERROR.getCode(), TokenExceptionCodeEnum.TOKEN_ERROR.getMessage())));
            return new CheckVO(false,token);
        }
        // 日志用的 - %X{token}
        MDC.put(JwtConstant.TOKEN, token);
        // 验证用户状态
        checkUserStatus(token);
        return new CheckVO(true,token);
    }



    /**
     * 检查用户状态
     * @param token token
     * @exception  Exception 用户状态异常
     */
    private void checkUserStatus(String token) throws Exception{
        // 检查用户状态
        checkTokenInterceptor.checkUserStatus(JwtService.getSubject(token));
    }

    /**
     * 刷新缓存 - redis中才有用
     * @param token token
     * @param method method
     */
    private void refreshToken(String token, Method method) {
        // token缓存刷新
        try {

            // 全局设置刷新状态 false: 不刷新
            if(jwtConfig.getCallRefreshToken() && (!method.isAnnotationPresent(NotRefreshToken.class))){
                    // 每次接口进来都要属性 token缓存。刷新方式请自主实现
                    checkTokenInterceptor.refreshToken(JwtService.getSubject(token));
            }
        }catch (Exception e){
            log.warn("token缓存刷新失败",e);
        }
    }

    private  void getCheckTokenInterceptor (){
        checkTokenInterceptor = ExtensionLoader.getExtensionLoader(CheckTokenInterceptor.class).getJoin("defaultInterceptor");
    }

}
