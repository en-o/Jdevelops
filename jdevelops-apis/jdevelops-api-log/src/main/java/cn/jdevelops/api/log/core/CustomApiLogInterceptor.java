package cn.jdevelops.api.log.core;


import cn.jdevelops.api.log.server.ApiLogSave;
import cn.jdevelops.interceptor.api.ApiFinallyInterceptor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 利用注解并自定义接口日志的处理
 * @author tan
 */
@Component
@Order(1)
public class CustomApiLogInterceptor implements ApiFinallyInterceptor {

    private final ApiLogSave apiLogSave;

    public CustomApiLogInterceptor(ApiLogSave apiLogSave) {
        this.apiLogSave = apiLogSave;
    }

    @Override
    public void finallys(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        ApiFinallyInterceptor.super.finallys(request, response, handler, ex);
        apiLogSave.saveLog(request, response, handler, ex);
    }
}
