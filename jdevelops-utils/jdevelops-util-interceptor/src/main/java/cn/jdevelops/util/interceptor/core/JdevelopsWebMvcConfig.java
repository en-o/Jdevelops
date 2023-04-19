package cn.jdevelops.util.interceptor.core;


import cn.jdevelops.util.interceptor.api.*;
import cn.jdevelops.util.interceptor.chain.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 自定义拦截器责任链后 自动注册拦截器到spring
 * @author tnnn
 */
@AutoConfiguration
public class JdevelopsWebMvcConfig implements WebMvcConfigurer {


    /**
     * 拦截器列表
     */
    private final List<ApiAfterInterceptor> afterInterceptors;
    private final List<ApiAsyncInterceptor> asyncInterceptors;
    private final List<ApiBeforeInterceptor> beforeInterceptors;
    private final List<ApiFinallyInterceptor> finallyInterceptors;
    private final List<ApiInterceptor> apiInterceptors;

    public JdevelopsWebMvcConfig(List<ApiAfterInterceptor> afterInterceptors,
                                 List<ApiAsyncInterceptor> asyncInterceptors,
                                 List<ApiBeforeInterceptor> beforeInterceptors,
                                 List<ApiFinallyInterceptor> finallyInterceptors,
                                 List<ApiInterceptor> apiInterceptors) {
        this.afterInterceptors = Objects.isNull(afterInterceptors)?new ArrayList<>():afterInterceptors;
        this.asyncInterceptors = Objects.isNull(asyncInterceptors)?new ArrayList<>():asyncInterceptors;
        this.beforeInterceptors = Objects.isNull(beforeInterceptors)?new ArrayList<>():beforeInterceptors;
        this.finallyInterceptors = Objects.isNull(finallyInterceptors)?new ArrayList<>():finallyInterceptors;
        this.apiInterceptors = Objects.isNull(apiInterceptors)?new ArrayList<>():apiInterceptors;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        // 单步拦截
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Object handler) throws Exception {
                ApiBeforeInterceptorChain beforeChain = new ApiBeforeInterceptorChain(beforeInterceptors);
                return beforeChain.execute(request, response,handler);
            }

            @Override
            public void postHandle(HttpServletRequest request,
                                   HttpServletResponse response,
                                   Object handler,
                                   ModelAndView modelAndView) throws Exception {
                ApiAfterInterceptorChain afterChain = new ApiAfterInterceptorChain(afterInterceptors);
                afterChain.execute(request, response, handler, modelAndView);
            }

            @Override
            public void afterCompletion(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Object handler,
                                        Exception ex) throws Exception {
                ApiFinallyInterceptorChain finallyChain = new ApiFinallyInterceptorChain(finallyInterceptors);
                finallyChain.execute(request, response, handler, ex);
            }
        });

        // 同步拦截
        ApiInterceptorChain apiChains = new ApiInterceptorChain(apiInterceptors);
        apiChains.execute().forEach(registry::addInterceptor);
        // 异步拦截
        registry.addInterceptor(new AsyncHandlerInterceptor(){
            @Override
            public void afterConcurrentHandlingStarted(HttpServletRequest request,
                                                       HttpServletResponse response,
                                                       Object handler) throws Exception {
                ApiAsyncInterceptorChain asyncInterceptor = new ApiAsyncInterceptorChain(asyncInterceptors);
                asyncInterceptor.execute(request, response, handler);
            }
        });
    }
}
