package cn.jdevelops.util.interceptor.chain;

import cn.jdevelops.util.interceptor.api.ApiAfterInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 后置拦截器责任链
 *  - 交给spring管理
 * @author tnnn
 */
public class ApiAfterInterceptorChain {


    /**
     * 拦截器列表
     */
    private final List<ApiAfterInterceptor> interceptors;

    public ApiAfterInterceptorChain(List<ApiAfterInterceptor> interceptors) {
        this.interceptors = interceptors;
    }


    /**
     * 执行处理
     * @param request 请求对象
     * @param response 响应对象
     * @param handler 被调用的处理器对象，本质是一个方法对象，对反射中的Method对象进行了再包装，对方法进行封装加强，操作原始对象
     * @param modelAndView  modelAndView
     * @throws Exception Exception
     */
    public void execute(HttpServletRequest request,
                        HttpServletResponse response,
                        Object handler,
                        ModelAndView modelAndView) throws Exception {
        // 循环执行
        for (ApiAfterInterceptor chain : interceptors) {
            chain.after(request, response, handler, modelAndView);
        }
    }

}
