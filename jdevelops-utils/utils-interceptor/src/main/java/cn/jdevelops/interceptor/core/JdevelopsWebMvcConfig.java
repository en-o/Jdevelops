package cn.jdevelops.interceptor.core;


import cn.jdevelops.interceptor.chain.ApiAfterInterceptorChain;
import cn.jdevelops.interceptor.chain.ApiAsyncInterceptorChain;
import cn.jdevelops.interceptor.chain.ApiBeforeInterceptorChain;
import cn.jdevelops.interceptor.chain.ApiFinallyInterceptorChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器责任链后 自动注册拦截器到spring
 * @author tnnn
 */
@Configuration
public class JdevelopsWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Object handler) throws Exception {
                ApiBeforeInterceptorChain beforeChain = new ApiBeforeInterceptorChain();
                return beforeChain.execute(request, response,handler);
            }

            @Override
            public void postHandle(HttpServletRequest request,
                                   HttpServletResponse response,
                                   Object handler,
                                   ModelAndView modelAndView) throws Exception {
                ApiAfterInterceptorChain afterChain = new ApiAfterInterceptorChain();
                afterChain.execute(request, response, handler, modelAndView);
            }

            @Override
            public void afterCompletion(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Object handler,
                                        Exception ex) throws Exception {
                ApiFinallyInterceptorChain finallyChain = new ApiFinallyInterceptorChain();
                finallyChain.execute(request, response, handler, ex);
            }
        });

        registry.addInterceptor(new AsyncHandlerInterceptor(){
            @Override
            public void afterConcurrentHandlingStarted(HttpServletRequest request,
                                                       HttpServletResponse response,
                                                       Object handler) throws Exception {
                ApiAsyncInterceptorChain asyncInterceptor = new ApiAsyncInterceptorChain();
                asyncInterceptor.execute(request, response, handler);
            }
        });
    }
}
