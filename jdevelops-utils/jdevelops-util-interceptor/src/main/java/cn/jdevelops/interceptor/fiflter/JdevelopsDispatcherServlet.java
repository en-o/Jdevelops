package cn.jdevelops.interceptor.fiflter;

import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* 自定义 DispatcherServlet 来分派 JdevelopsHttpServletRequestWrapper
 * @author tan
*/
public class JdevelopsDispatcherServlet extends DispatcherServlet {

    /**
     * 包装成我们自定义的request
     * @param request 请求体
     * @param response 响应体
     * @throws Exception Exception
     */
    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.doDispatch(new JdevelopsHttpServletRequestWrapper(request), response);
    }
}
