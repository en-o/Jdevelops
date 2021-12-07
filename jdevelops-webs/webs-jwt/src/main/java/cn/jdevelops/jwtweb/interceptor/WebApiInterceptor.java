package cn.jdevelops.jwtweb.interceptor;

import com.alibaba.fastjson.JSON;
import cn.jdevelops.enums.result.ResultCodeEnum;
import cn.jdevelops.jwtweb.annotation.ApiMapping;
import cn.jdevelops.jwtweb.holder.ApplicationContextHolder;
import cn.jdevelops.jwtweb.server.CheckTokenInterceptor;
import cn.jdevelops.jwtweb.server.impl.DefaultInterceptor;
import cn.jdevelops.result.result.ResultVO;
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
                return  check(request, response, handler, logger);
            }else{
                return true;
            }
        }else{
            return  check(request, response, handler, logger);
        }
    }

    private boolean check(HttpServletRequest request, HttpServletResponse response, Object handler, Logger logger) throws IOException {
            String token = getToken(request);
            getCheckTokenInterceptor();
            boolean flag = checkTokenInterceptor.checkToken(token);
            logger.info("需要验证token,校验结果：{}", flag);
            if (!flag) {
                response.setHeader("content-type", "application/json;charset=UTF-8");
                response.getWriter().write(JSON.toJSONString(ResultVO.fail(ResultCodeEnum.TokenError.getCode(), "无效的token")));
                return false;
            }
            MDC.put("token", token);
            return true;
    }

    private String getToken(HttpServletRequest request) {
        final String tokenName = "token";
        String token = request.getHeader(tokenName);
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        token = request.getParameter(tokenName);
        return token;
    }

    private  void getCheckTokenInterceptor (){
        try {
            checkTokenInterceptor = ApplicationContextHolder.get().getBean(CheckTokenInterceptor.class);
        } catch (Exception e) {
            // 使用默认拦截
            checkTokenInterceptor = ApplicationContextHolder.get().getBean(DefaultInterceptor.class);
        }
    }
}
