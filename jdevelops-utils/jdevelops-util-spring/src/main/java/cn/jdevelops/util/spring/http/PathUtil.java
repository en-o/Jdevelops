package cn.jdevelops.util.spring.http;

import org.springframework.util.AntPathMatcher;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * 路径相关
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-02-10 09:54
 */
public class PathUtil {
    /**
     * 过滤放行验证
     * 真 放行
     * 假 不放行
     * @param addPathPatterns 待放行列表
     * @param servletRequest servlet
     * @return Boolean
     */
    public static Boolean isOkPath(Set<String> addPathPatterns, ServletRequest servletRequest){
        if(servletRequest instanceof HttpServletRequest) {
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            return addPathPatterns.stream().anyMatch(e ->antPathMatcher.match(e,((HttpServletRequest) servletRequest).getServletPath()));
        }
        return false;
    }
}
