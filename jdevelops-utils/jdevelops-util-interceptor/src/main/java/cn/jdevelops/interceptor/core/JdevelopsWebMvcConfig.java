package cn.jdevelops.interceptor.core;


import cn.jdevelops.interceptor.api.ApiAfterInterceptor;
import cn.jdevelops.interceptor.api.ApiAsyncInterceptor;
import cn.jdevelops.interceptor.api.ApiBeforeInterceptor;
import cn.jdevelops.interceptor.api.ApiFinallyInterceptor;
import cn.jdevelops.interceptor.chain.ApiAfterInterceptorChain;
import cn.jdevelops.interceptor.chain.ApiAsyncInterceptorChain;
import cn.jdevelops.interceptor.chain.ApiBeforeInterceptorChain;
import cn.jdevelops.interceptor.chain.ApiFinallyInterceptorChain;
import cn.jdevelops.interceptor.fiflter.JdevelopsDispatcherServlet;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.DispatcherServlet;
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

    public JdevelopsWebMvcConfig(List<ApiAfterInterceptor> afterInterceptors,
                                 List<ApiAsyncInterceptor> asyncInterceptors,
                                 List<ApiBeforeInterceptor> beforeInterceptors,
                                 List<ApiFinallyInterceptor> finallyInterceptors) {
        this.afterInterceptors = Objects.isNull(afterInterceptors)?new ArrayList<>():afterInterceptors;
        this.asyncInterceptors = Objects.isNull(asyncInterceptors)?new ArrayList<>():asyncInterceptors;
        this.beforeInterceptors = Objects.isNull(beforeInterceptors)?new ArrayList<>():beforeInterceptors;
        this.finallyInterceptors = Objects.isNull(finallyInterceptors)?new ArrayList<>():finallyInterceptors;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

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

    @Bean
    @ConditionalOnSingleCandidate
    public DispatcherServlet dispatcherServlet() {
        return new JdevelopsDispatcherServlet();
    }
}
