package cn.jdevelops.interceptor.api;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义的接口后置拦截器  - 模仿 AsyncHandlerInterceptor
 * @author tnnn
 */
public interface ApiAsyncInterceptor {

    /**
     * 异步请求的拦截
     *  - afterConcurrentHandlingStarted
     * @param request 请求对象
     * @param response 响应对象
     * @param handler 被调用的处理器对象，本质是一个方法对象，对反射中的Method对象进行了再包装，对方法进行封装加强，操作原始对象
     * @throws Exception Exception
     */
    default void async(HttpServletRequest request,
                       HttpServletResponse response,
                       Object handler) throws Exception {
        // 这里可以添加你的业务逻辑，例如异步请求的拦截等
    }

}
