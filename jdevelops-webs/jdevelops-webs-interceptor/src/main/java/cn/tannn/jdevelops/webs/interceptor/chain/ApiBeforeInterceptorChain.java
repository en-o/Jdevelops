package cn.tannn.jdevelops.webs.interceptor.chain;


import cn.tannn.jdevelops.webs.interceptor.ApiBeforeInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 前置拦截器责任链
 * - 交给spring管理
 *
 * @author tnnn
 */
public class ApiBeforeInterceptorChain {


    /**
     * 拦截器列表
     */
    private final List<ApiBeforeInterceptor> interceptors;

    public ApiBeforeInterceptorChain(List<ApiBeforeInterceptor> interceptors) {
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
    public boolean execute(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) throws Exception {
        boolean result = true;
        // 循环执行
        if(interceptors != null){
            for (ApiBeforeInterceptor chain : interceptors) {
                boolean before = chain.before(request, response, handler);
                if (!before) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

}
