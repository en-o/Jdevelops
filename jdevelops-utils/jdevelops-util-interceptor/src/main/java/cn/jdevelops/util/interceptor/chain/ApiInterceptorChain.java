package cn.jdevelops.util.interceptor.chain;

import cn.jdevelops.util.interceptor.api.ApiInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的接口后置拦截器责任链
 * - 交给spring管理
 *
 * @author tnnn
 */
public class ApiInterceptorChain {

    private static final Logger logger = LoggerFactory.getLogger(ApiInterceptorChain.class);

    /**
     * 拦截器列表
     */
    private final List<ApiInterceptor> interceptors;

    public ApiInterceptorChain(List<ApiInterceptor> interceptors) {
        this.interceptors = interceptors;
    }


    /**
     * 执行处理
     *
     * @throws Exception Exception
     */
    public List<HandlerInterceptor> execute() {
        List<HandlerInterceptor> handlerInterceptors = new ArrayList<>();
        // 循环执行
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
        return handlerInterceptors;
    }

}
