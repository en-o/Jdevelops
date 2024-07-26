package cn.tannn.jdevelops.webs.interceptor.chain;

import cn.tannn.jdevelops.webs.interceptor.ApiInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的接口拦截器责任链
 * - 交给spring管理
 *
 * @author tnnn
 */
public class ApiInterceptorChain {


    /**
     * 拦截器列表
     */
    private final List<ApiInterceptor> interceptors;

    public ApiInterceptorChain(List<ApiInterceptor> interceptors) {
        this.interceptors = interceptors;
    }


    /**
     * 执行处理
     */
    public List<HandlerInterceptor> execute() {
        List<HandlerInterceptor> handlerInterceptors = new ArrayList<>();
        // 循环执行
        if (interceptors != null) {
            for (ApiInterceptor chain : interceptors) {
                handlerInterceptors.add(
                        new HandlerInterceptor() {
                            @Override
                            public boolean preHandle(HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     Object handler) throws Exception {
                                return chain.before(request, response, handler);
                            }

                            @Override
                            public void postHandle(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   Object handler,
                                                   ModelAndView modelAndView) throws Exception {
                                chain.after(request, response, handler, modelAndView);
                            }

                            @Override
                            public void afterCompletion(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        Object handler,
                                                        Exception ex) throws Exception {
                                chain.finallys(request, response, handler, ex);
                            }
                        });
            }
        }
        return handlerInterceptors;
    }

}
