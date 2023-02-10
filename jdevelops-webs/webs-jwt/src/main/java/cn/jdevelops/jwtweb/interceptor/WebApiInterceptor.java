package cn.jdevelops.jwtweb.interceptor;

import cn.jdevelops.enums.result.TokenExceptionCodeEnum;
import cn.jdevelops.jwt.bean.JwtBean;
import cn.jdevelops.jwt.util.JwtUtil;
import cn.jdevelops.jwtweb.util.JwtWebUtil;
import cn.jdevelops.jwtweb.vo.CheckVO;
import cn.jdevelops.result.custom.ExceptionResultWrap;
import cn.jdevelops.jwt.annotation.ApiMapping;
import cn.jdevelops.jwt.annotation.NotRefreshToken;
import cn.jdevelops.jwt.constant.JwtConstant;
import cn.jdevelops.spi.ExtensionLoader;
import com.alibaba.fastjson.JSON;
import cn.jdevelops.jwtweb.server.CheckTokenInterceptor;
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

    private final JwtBean jwtBean;

    public WebApiInterceptor(JwtBean jwtBean) {
        this.jwtBean = jwtBean;
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
        String  token = JwtWebUtil.getToken(request,jwtBean.getCookie());
        // 验证token
        boolean flag = checkTokenInterceptor.checkToken(token);
        logger.info("需要验证token,校验结果：{},token:{}", flag,token);
        if (!flag) {
            response.setHeader("content-type", "application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(ExceptionResultWrap.error(TokenExceptionCodeEnum.TOKEN_ERROR.getCode(), TokenExceptionCodeEnum.TOKEN_ERROR.getMessage())));
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
        checkTokenInterceptor.checkUserStatus(JwtUtil.getSubject(token));
    }

    /**
     * 刷新缓存
     * @param token token
     * @param method method
     */
    private void refreshToken(String token, Method method) {
        // token缓存刷新
        try {
            //  此注解表示不刷新缓存
            if(!method.isAnnotationPresent(NotRefreshToken.class)){
                // 每次接口进来都要属性 token缓存。刷新方式请自主实现
                checkTokenInterceptor.refreshToken(JwtUtil.getSubject(token));
            }
        }catch (Exception e){
            log.warn("token缓存刷新失败",e);
        }
    }

    private  void getCheckTokenInterceptor (){
        checkTokenInterceptor = ExtensionLoader.getExtensionLoader(CheckTokenInterceptor.class).getJoin("defaultInterceptor");
    }

}
