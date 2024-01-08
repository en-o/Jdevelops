package cn.jdevelops.util.authorization.error;

import cn.jdevelops.util.authorization.error.respone.ResponseUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 异常过滤器
 * @author tan
 */
public class CustomExceptionTranslationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }catch (Exception e) {
            if (e instanceof AuthenticationException || e instanceof AccessDeniedException) {
                throw e;
            }
            //非AuthenticationException、AccessDeniedException异常，则直接响应
            ResponseUtil.exceptionResponse(response, e);
        }

    }
}
