package cn.jdevelops.jdbctemplate.aspect;

import cn.jdevelops.jdbctemplate.annotation.Query;
import cn.jdevelops.jdbctemplate.util.AnnotationParse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 修改方法的返回值
 *
 * @author tn
 * @version 1
 * @date 2020/4/30 2:32
 */
@Aspect
@Component
@Lazy(false)
public class QueryAop {

    private static final Logger LOG = LoggerFactory.getLogger(QueryAop.class);
    @Resource
    private JdbcTemplate jdevelopsJdbcTemplate;

    /**
     * 设置jpa注解为切入点
     * 环绕通知：灵活自由的在目标方法中切入代码
     *
     * @param pjp pjp
     */
    @Around(value = "@annotation(cn.jdevelops.jdbctemplate.annotation.Query)")
    public Object doAfterReturning(ProceedingJoinPoint pjp) throws Throwable {
        //执行方法
        Object rvt = pjp.proceed();
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        /*reBean 返回值类型*/
        Query query = method.getAnnotation(Query.class);

        if (query != null) {
            Object resolver = AnnotationParse.newInstance().resolver(pjp, query.value());
            if (query.clazz() == String.class || query.clazz() == Integer.class) {
                return getJdbcTemplateSqlContextBaseType(rvt, resolver, query);
            }
            return getJdbcTemplateSqlContextMapType(rvt, resolver, query);

        }
        return rvt;
    }


    /**
     * 返回值为 String, Integer 基本类型时使用的方法
     *
     * @param rvt      执行方法
     * @param resolver sql
     * @param query    擦汗寻注解
     * @return Object 数据
     */
    public Object getJdbcTemplateSqlContextBaseType(Object rvt, Object resolver, Query query) {
        if (rvt instanceof List) {
            return jdevelopsJdbcTemplate.queryForList(resolver.toString(),
                    query.clazz());

        } else {
            return jdevelopsJdbcTemplate.queryForObject(resolver.toString(),
                    query.clazz());
        }
    }


    /**
     * 返回值为Bean, Map 型时使用的方法
     *
     * @param rvt      执行方法
     * @param resolver sql
     * @param query    擦汗寻注解
     * @return Object 数据
     */
    public Object getJdbcTemplateSqlContextMapType(Object rvt, Object resolver, Query query) {
        BeanPropertyRowMapper beanPropertyRowMapper = new BeanPropertyRowMapper<>(query.clazz());
        if (rvt instanceof List) {
            return jdevelopsJdbcTemplate.query(resolver.toString(),
                    beanPropertyRowMapper);

        } else {
            return jdevelopsJdbcTemplate.queryForObject(resolver.toString(),
                    beanPropertyRowMapper);
        }
    }
}
