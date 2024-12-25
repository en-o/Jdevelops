package cn.tannn.jdevelops.log.audit;

import jakarta.servlet.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 增加请求过滤器，确保在请求完成后清理上下文
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuditContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            AuditContextHolder.clearBatch();
            AuditContextHolder.clear();
        }
    }
}
