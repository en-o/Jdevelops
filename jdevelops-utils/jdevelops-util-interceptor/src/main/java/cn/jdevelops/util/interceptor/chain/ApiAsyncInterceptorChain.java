package cn.jdevelops.util.interceptor.chain;

import cn.jdevelops.util.interceptor.api.ApiAsyncInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 异步拦截器责任链
 * - 交给spring管理
 * @author tnnn
 */
public class ApiAsyncInterceptorChain {

    /**
     * 拦截器列表
     */
    private final List<ApiAsyncInterceptor> interceptors;

    public ApiAsyncInterceptorChain(List<ApiAsyncInterceptor> interceptors) {
        this.interceptors = interceptors;
    }


    /**
     * 执行处理
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  被调用的处理器对象，本质是一个方法对象，对反射中的Method对象进行了再包装，对方法进行封装加强，操作原始对象
     * @throws Exception Exception
     */
    public void execute(HttpServletRequest request,
                        HttpServletResponse response,
                        Object handler) throws Exception {
        // 循环执行
        for (ApiAsyncInterceptor chain : interceptors) {
            chain.async(request, response, handler);
        }
    }

}
