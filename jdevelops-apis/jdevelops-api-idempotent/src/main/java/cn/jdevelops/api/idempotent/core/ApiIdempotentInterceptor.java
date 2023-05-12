package cn.jdevelops.api.idempotent.core;

import cn.jdevelops.api.idempotent.annotation.ApiIdempotent;
import cn.jdevelops.api.idempotent.service.IdempotentService;
import cn.jdevelops.util.interceptor.api.ApiBeforeInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 接口幂等性拦截器
 * @author @author 网络
 */
@AutoConfiguration
@Import(IdempotentService.class)
@Order(3)
public class ApiIdempotentInterceptor implements ApiBeforeInterceptor {

    private final IdempotentService idempotentService;

    public ApiIdempotentInterceptor(IdempotentService idempotentService) {
        this.idempotentService = idempotentService;
    }


    @Override
    public boolean before(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        ApiIdempotent methodAnnotation = method.getAnnotation(ApiIdempotent.class);
        if (methodAnnotation != null) {
            // 幂等性校验, 校验通过则放行, 校验失败则抛出异常, 并通过统一异常处理返回友好提示
           return check(request, methodAnnotation);
        }
        return true;
    }

    private boolean check(HttpServletRequest request, ApiIdempotent methodAnnotation) {
        return idempotentService.checkApiRedo(request, methodAnnotation);
    }


}
