package cn.tannn.jdevelops.jdectemplate.proxysql.core;

import cn.tannn.jdevelops.annotations.jdbctemplate.proxysql.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * CGLIB 代理 - 完成 jdbcTemplate query请求的处理
 * <p> 目标类不能是 final 类，且方法也不能是 final 或 private </p>
 */
public class JdbcCglibMethodInterceptor implements MethodInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcCglibMethodInterceptor.class);


    private final JdbcTemplate jdbcTemplate;
    private final Class<? extends Annotation> annotation;

    public JdbcCglibMethodInterceptor(JdbcTemplate jdbcTemplate
            , Class<? extends Annotation> annotation) {
        this.jdbcTemplate = jdbcTemplate;
        this.annotation = annotation;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        LOG.debug(" jdbctemplate ========> cglib Executing query for method: {}", method.getName());
        Annotation ann = method.getAnnotation(annotation);
        // todo 后期在这里处理 query install delete update 的选择
        if(ann ==  null){
            LOG.info("正常方法：{}，不需要代理",method.getName());
            return proxy.invokeSuper(obj, args);
        }else if (ann instanceof Query query) {
            return ProxyCoreFun.query(query, jdbcTemplate, method, args);
        }else {
            LOG.warn("{}还没实现",ann.annotationType().getName());
            return proxy.invokeSuper(obj, args);
        }
    }
}
