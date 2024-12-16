package cn.tannn.jdevelops.jdectemplate.core;

import cn.tannn.jdevelops.annotations.jdbctemplate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理 - 完成 jdbcTemplate query请求的处理
 * <p> InvocationHandler 是Java中用于实现动态代理的一个接口 </p>
 * <p> Java 的 Proxy 类只能为接口创建代理 </p>
 */
public class JdbcProxyMethodInterceptor implements InvocationHandler {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcProxyMethodInterceptor.class);

    /**
     * 目标对象，即实际执行方法的对象
     */
    private final Object target;
    private final JdbcTemplate jdbcTemplate;
    private final Class<? extends Annotation> annotation;

    /**
     * @param target 目标类
     * @param jdbcTemplate {@link JdbcTemplate}
     * @param annotation 目标注解 {@link Query}
     */
    public JdbcProxyMethodInterceptor(Object target
            , JdbcTemplate jdbcTemplate
            , Class<? extends Annotation> annotation) {
        this.target = target;
        this.jdbcTemplate = jdbcTemplate;
        this.annotation = annotation;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        LOG.debug(" jdbctemplate ========> jdk Executing query for method: {}", method.getName());
        Annotation ann = method.getAnnotation(annotation);
        // todo 后期在这里处理 query install delete update 的选择
        if(ann ==  null){
            LOG.info("正常方法：{}，不需要代理",method.getName());
            return method.invoke(target, args);
        }else if (ann instanceof Query query) {
            return ProxyCoreFun.query(query, jdbcTemplate, method, args);
        }else {
            LOG.warn("{}还没实现",ann.annotationType().getName());
            return method.invoke(target, args);
        }
    }
}
