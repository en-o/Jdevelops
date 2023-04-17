package cn.jdevelops.sboot.web.util;


import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * 路径匹配
 * @author tn
 */
public class PathMatcherHolder {
    public static PathMatcher pathMatcher;

    public PathMatcherHolder() {
    }

    public static PathMatcher getPathMatcher() {
        if (pathMatcher == null) {
            pathMatcher = new AntPathMatcher();
        }

        return pathMatcher;
    }

    public static void setPathMatcher(PathMatcher pathMatcher) {
        PathMatcherHolder.pathMatcher = pathMatcher;
    }
}
