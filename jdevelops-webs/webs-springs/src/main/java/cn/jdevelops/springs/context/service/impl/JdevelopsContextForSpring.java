package cn.jdevelops.springs.context.service.impl;

import cn.jdevelops.springs.context.entity.JdevelopsRequest;
import cn.jdevelops.springs.context.entity.JdevelopsResponse;
import cn.jdevelops.springs.context.service.JdevelopsContext;
import cn.jdevelops.springs.context.servlet.JdevelopsRequestForServlet;
import cn.jdevelops.springs.context.servlet.JdevelopsResponseForServlet;
import cn.jdevelops.springs.context.util.PathMatcherHolder;
import cn.jdevelops.springs.context.util.SpringMVCUtil;

/**
 * Spring boot 上下文
 * @author tnnn
 */
public class JdevelopsContextForSpring implements JdevelopsContext {
    public JdevelopsContextForSpring() {
    }

    @Override
    public JdevelopsRequest getRequest() {
        return new JdevelopsRequestForServlet(SpringMVCUtil.getRequest());
    }

    @Override
    public JdevelopsResponse getResponse() {
        return new JdevelopsResponseForServlet(SpringMVCUtil.getResponse());
    }

    @Override
    public boolean matchPath(String pattern, String path) {
        return PathMatcherHolder.getPathMatcher().match(pattern, path);
    }

    @Override
    public boolean isValid() {
        return SpringMVCUtil.isWeb();
    }
}
