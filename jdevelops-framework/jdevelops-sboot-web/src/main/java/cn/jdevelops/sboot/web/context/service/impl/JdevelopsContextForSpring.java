package cn.jdevelops.sboot.web.context.service.impl;


import cn.jdevelops.sboot.web.context.service.JdevelopsContext;
import cn.jdevelops.sboot.web.context.servlet.JdevelopsRequestForServlet;
import cn.jdevelops.sboot.web.context.servlet.JdevelopsResponseForServlet;
import cn.jdevelops.sboot.web.util.PathMatcherHolder;
import cn.jdevelops.sboot.web.util.SpringMVCUtil;
import cn.jdevelops.sboot.web.entity.http.JdevelopsRequest;
import cn.jdevelops.sboot.web.entity.http.JdevelopsResponse;

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
