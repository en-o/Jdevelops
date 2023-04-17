package cn.jdevelops.sboot.web.util;

import cn.jdevelops.sboot.web.exception.JHttpException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SpringMVCUtil
 * @author tan
 */
public class SpringMVCUtil {
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            throw new JHttpException("非Web上下文无法获取Request");
        } else {
            return servletRequestAttributes.getRequest();
        }
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            throw new JHttpException("非Web上下文无法获取Response");
        } else {
            return servletRequestAttributes.getResponse();
        }
    }

    public static boolean isWeb() {
        return RequestContextHolder.getRequestAttributes() != null;
    }
}
