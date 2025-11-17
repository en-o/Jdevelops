package cn.tannn.jdevelops.webs.interceptor.fiflter;

import cn.tannn.jdevelops.webs.interceptor.core.WebsInterceptorConfig;
import cn.tannn.jdevelops.webs.interceptor.util.RequestUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/***
 * HttpServletRequest 过滤器
 * 解决: request.getInputStream()只能读取一次的问题
 * <p>  urlPatterns = "/" 拦截所有请求</p>
 * @author link
 * <a href="https://blog.csdn.net/itdragons/article/details/106632260">参考</a>
 * 目标: 流可重复读
 */
@Component
@WebFilter(filterName = "JdevelopsHttpServletRequestFilter", urlPatterns = "/")
@Order(10000)
public class JdevelopsHttpServletRequestFilter implements Filter {

    private  final WebsInterceptorConfig websInterceptorConfig;

    public JdevelopsHttpServletRequestFilter(WebsInterceptorConfig websInterceptorConfig) {
        this.websInterceptorConfig = Objects.requireNonNullElseGet(websInterceptorConfig, WebsInterceptorConfig::new);
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpRequest.getRequestURI();

        // 检查是否是放行请求
        if (isGreenRequest(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if(websInterceptorConfig.isContentType()){
            // 强制设置 Content-Type
            servletResponse.setContentType(websInterceptorConfig.getMediaType().toString());

        }
        // 判断是否是multipart/form-data请求
        if (!RequestUtil.isMultipartContent(httpRequest)) {
            // 创建可重复读的请求包装器
            ServletRequest requestWrapper = new JdevelopsHttpServletRequestWrapper(httpRequest);
            // 获取请求中的流如何，将取出来的字符串，再次转换成流，然后把它放入到新request对象中
            // 在chain.doFiler方法中传递新的request对象
            if(null == requestWrapper) {
                filterChain.doFilter(servletRequest, servletResponse);
            }else {
                filterChain.doFilter(requestWrapper, servletResponse);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * 检查是否是 需要放行请求
     * <p> 少量判断当前方法可以满足，数据量后期如果变大可以考虑使用 Trie 树等数据结构优化（DFA）
     * @param requestURI 当前请求
     * @return true 是放行请求  false 不是放行请求
     */
    private boolean isGreenRequest(String requestURI) {
        for (String excludePath : websInterceptorConfig.gainFinalExcludePaths()) {
            if (excludePath.endsWith("/*") && requestURI.startsWith(excludePath.substring(0, excludePath.length() - 2))) {
                return true;
            } else if (requestURI.equals(excludePath)) {
                return true;
            }
        }
        return false;
    }
}

