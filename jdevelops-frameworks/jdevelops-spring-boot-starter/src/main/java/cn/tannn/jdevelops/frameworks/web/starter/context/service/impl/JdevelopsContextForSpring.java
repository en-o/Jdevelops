package cn.tannn.jdevelops.frameworks.web.starter.context.service.impl;


import cn.tannn.jdevelops.frameworks.web.starter.context.service.JdevelopsContext;
import cn.tannn.jdevelops.frameworks.web.starter.context.servlet.JdevelopsRequestForServlet;
import cn.tannn.jdevelops.frameworks.web.starter.context.servlet.JdevelopsResponseForServlet;
import cn.tannn.jdevelops.frameworks.web.starter.entity.http.JdevelopsRequest;
import cn.tannn.jdevelops.frameworks.web.starter.entity.http.JdevelopsResponse;
import cn.tannn.jdevelops.frameworks.web.starter.util.PathMatcherHolder;
import cn.tannn.jdevelops.frameworks.web.starter.util.SpringMVCUtil;

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
