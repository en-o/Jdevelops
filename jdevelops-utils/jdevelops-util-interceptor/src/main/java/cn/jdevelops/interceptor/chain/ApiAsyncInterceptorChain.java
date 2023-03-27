package cn.jdevelops.interceptor.chain;

import cn.jdevelops.interceptor.api.ApiAsyncInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 异步拦截器责任链
 * - 交给spring管理
 * @author tnnn
 */
public class ApiAsyncInterceptorChain {

    private static final Logger logger = LoggerFactory.getLogger(ApiFinallyInterceptorChain.class);

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
            try {
                chain.async(request, response, handler);
            }catch (Exception e){
                // 错误不干扰
                logger.error("自定义的异步拦截器异常", e);
            }
        }
    }

}
