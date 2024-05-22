package cn.tannn.jdevelops.webs.interceptor.fiflter;

import cn.tannn.jdevelops.webs.interceptor.util.RequestUtil;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/***
 * HttpServletRequest 过滤器
 * 解决: request.getInputStream()只能读取一次的问题
 * @author link
 * <a href="https://blog.csdn.net/itdragons/article/details/106632260">参考</a>
 * 目标: 流可重复读
 */
@Component
@WebFilter(filterName = "JdevelopsHttpServletRequestFilter", urlPatterns = "/")
@Order(10000)
public class JdevelopsHttpServletRequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        servletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        // 判断是否是multipart/form-data请求
        if (!RequestUtil.isMultipartContent((HttpServletRequest)servletRequest)){
            if(servletRequest instanceof HttpServletRequest) {
                requestWrapper = new JdevelopsHttpServletRequestWrapper((HttpServletRequest) servletRequest);
            }
            // 获取请求中的流如何，将取出来的字符串，再次转换成流，然后把它放入到新request对象中
            // 在chain.doFiler方法中传递新的request对象
            if(null == requestWrapper) {
                filterChain.doFilter(servletRequest, servletResponse);
            }else {
                filterChain.doFilter(requestWrapper, servletResponse);
            }
        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }
}

