package cn.jdevelops.springs.scan;

import cn.jdevelops.springs.context.ContextManager;
import cn.jdevelops.springs.context.service.JdevelopsContext;
import cn.jdevelops.springs.context.util.PathMatcherHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.PathMatcher;

/**
 * bean注入 在注册之后
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-02-12 00:34
 */
public class SwBeanInject {

    /**
     * 设置 ContextManager 的值
     * @param jdevelopsContext JdevelopsContext
     */
    @Autowired(required = false)
    public void setJdevelopsContext(JdevelopsContext jdevelopsContext) {
        ContextManager.setJdevelopsContext(jdevelopsContext);
    }




    /**
     * 利用自动注入特性，获取Spring框架内部使用的路由匹配器
     *
     * @param pathMatcher 要设置的 pathMatcher
     */
    @Autowired(required = false)
    @Qualifier("mvcPathMatcher")
    public void setPathMatcher(PathMatcher pathMatcher) {
        PathMatcherHolder.setPathMatcher(pathMatcher);
    }

}
