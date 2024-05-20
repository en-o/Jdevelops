package cn.tannn.jdevelops.webs.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义的接口整个请求处理完毕拦截器  - 模仿 HandlerInterceptor
 * before -> after -> afterCompletion
 * @author tnnn
 */
public interface ApiFinallyInterceptor {

    /**
     * 整个请求处理完毕回调方法
     *  - afterCompletion
     * @param request 请求对象
     * @param response 响应对象
     * @param handler 被调用的处理器对象，本质是一个方法对象，对反射中的Method对象进行了再包装，对方法进行封装加强，操作原始对象
     * @param ex  Exception
     * @throws Exception Exception
     */
    default void finallys(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Object handler,
                                 Exception ex) throws Exception {
        // 这里可以添加你的业务逻辑，例如资源释放等
    }
}
