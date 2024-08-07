package cn.tannn.jdevelops.webs.interceptor.chain;

import cn.tannn.jdevelops.webs.interceptor.ApiFinallyInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 自定义的接口整个请求处理完毕拦截器责任链
 * - 交给spring管理
 *
 * @author tnnn
 */
public class ApiFinallyInterceptorChain {

    /**
     * 拦截器列表
     */
    private final List<ApiFinallyInterceptor> interceptors;

    public ApiFinallyInterceptorChain(List<ApiFinallyInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    /**
     * 执行处理
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  被调用的处理器对象，本质是一个方法对象，对反射中的Method对象进行了再包装，对方法进行封装加强，操作原始对象
     * @param ex       Exception
     * @throws Exception Exception
     */
    public void execute(HttpServletRequest request,
                        HttpServletResponse response,
                        Object handler,
                        Exception ex) throws Exception {
        if (interceptors != null) {
            // 循环执行
            for (ApiFinallyInterceptor chain : interceptors) {
                chain.finallys(request, response, handler, ex);
            }
        }
    }

}
