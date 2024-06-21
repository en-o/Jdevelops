package cn.tannn.jdevelops.jdectemplate.core;


import cn.tannn.jdevelops.jdectemplate.annotation.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * select 动态代理
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/2 14:37
 */
public class  QueryHandler implements InvocationHandler {
    private static final Logger LOG = LoggerFactory.getLogger(QueryHandler.class);
    /**
     * 需要添加代理的对象
     */
    final Class<?> service;
    final JdbcTemplate jdbcTemplate;

    public QueryHandler(Class<?> service, JdbcTemplate jdbcTemplate) {
        this.service = service;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Query dbQuery = method.getAnnotation(Query.class);
        if (dbQuery != null) {
            String sql = dbQuery.value();
            LOG.info("query sql : {}", sql);
        }
        return method.invoke(service, args);

    }


}
