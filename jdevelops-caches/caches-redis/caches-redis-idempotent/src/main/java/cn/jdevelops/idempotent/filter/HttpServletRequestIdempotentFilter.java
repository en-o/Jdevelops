package cn.jdevelops.idempotent.filter;

import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static cn.jdevelops.idempotent.util.ParamUtil.getBodyString;
import static cn.jdevelops.idempotent.util.ParamUtil.isMultipartContent;

/***
 * HttpServletRequest 过滤器
 * 解决: request.getInputStream()只能读取一次的问题
 * @author link
 * <a href="https://blog.csdn.net/itdragons/article/details/106632260">参考</a>
 * 目标: 流可重复读
 */
@Component
@WebFilter(filterName = "HttpServletRequestFilter", urlPatterns = "/")
@Order(10000)
public class HttpServletRequestIdempotentFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        servletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        // 判断是否是multipart/form-data请求
        if (!isMultipartContent((HttpServletRequest)servletRequest)){
            if(servletRequest instanceof HttpServletRequest) {
                requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
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

    @Override
    public void destroy() {

    }

    /***
     * HttpServletRequest 包装器
     * 解决: request.getInputStream()只能读取一次的问题
     * 目标: 流可重复读
     */
    public class RequestWrapper extends HttpServletRequestWrapper {

        /**
         * 请求体
         */
        private String mBody;

        public RequestWrapper(HttpServletRequest request) {
            super(request);
            // 将body数据存储起来
            mBody = getBody(request);
        }

        /**
         * 获取请求体
         * @param request 请求
         * @return 请求体
         */
        private String getBody(HttpServletRequest request) {
            return getBodyString(request);
        }

        /**
         * 获取请求体
         * @return 请求体
         */
        public String getBody() {
            return mBody;
        }

        @Override
        public BufferedReader getReader()  {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }

        @Override
        public ServletInputStream getInputStream() {
            // 创建字节数组输入流
            final ByteArrayInputStream basis = new ByteArrayInputStream(mBody.getBytes(StandardCharsets.UTF_8));

            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                @Override
                public int read() {
                    return basis.read();
                }
            };
        }
    }
}
