package cn.jdevelops.util.interceptor.api;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义的接口后置拦截器  - 模仿 HandlerInterceptor
 * before -> after -> afterCompletion
 * @author tnnn
 */
public interface ApiAfterInterceptor {

    /**
     * 后置处理类（在controller方法执行之后执行）
     *  - postHandle
     * @param request 请求对象
     * @param response 响应对象
     * @param handler 被调用的处理器对象，本质是一个方法对象，对反射中的Method对象进行了再包装，对方法进行封装加强，操作原始对象
     * @throws Exception Exception
     */
    default void after(HttpServletRequest request,
                       HttpServletResponse response,
                       Object handler,
                       ModelAndView modelAndView) throws Exception {
        // 这里可以添加你的业务逻辑，例如日志记录、性能监控等
    }

}
