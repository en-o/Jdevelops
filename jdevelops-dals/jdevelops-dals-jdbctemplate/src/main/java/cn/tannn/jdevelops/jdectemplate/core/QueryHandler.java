package cn.tannn.jdevelops.jdectemplate.core;

import cn.tannn.jdevelops.annotations.jdbctemplate.Query;
import cn.tannn.jdevelops.jdectemplate.util.AnnotationParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import static cn.tannn.jdevelops.jdectemplate.util.InteriorJdbcTemplateUtil.getJdbcTemplateSql;

/**
 * 动态代理
 */
public class QueryHandler implements InvocationHandler {

    private static final Logger LOG = LoggerFactory.getLogger(QueryHandler.class);


    private final JdbcTemplate jdbcTemplate;
    private final Class<? extends Annotation> annotation;

    public QueryHandler(JdbcTemplate jdbcTemplate, Class<? extends Annotation> annotation) {
        this.jdbcTemplate = jdbcTemplate;
        this.annotation = annotation;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(annotation)) {
            LOG.debug("jdbctemplate ========> Executing query for method: {}", method.getName());
            Annotation ann = method.getAnnotation(annotation);
            String sql = null;
            boolean tryc = false;

            if (ann instanceof Query) {
                Query q = (Query) ann;
                sql = q.value();
                tryc = q.tryc();
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("jdbctemplate ========> query sql : {},  args:{}", sql, args);
            }
            if (sql == null) {
                return null;
            }
            // 返回对象 - List
            String resultRawType;
            // 返回对象 - bean
            String resultActualType;
            // 填充占位符，获取真实的sql
            Object resolverSql = AnnotationParse.newInstance().resolver(method, args, sql);
            if (method.getGenericReturnType() instanceof ParameterizedType) {
                ParameterizedType genericReturnType = (ParameterizedType) method.getGenericReturnType();
                resultRawType = genericReturnType.getRawType().getTypeName();
                resultActualType = genericReturnType.getActualTypeArguments()[0].getTypeName();
            } else {
                resultRawType = method.getGenericReturnType().getTypeName();
                resultActualType = resultRawType;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("jdbctemplate ========> resultRawType : {},  resultActualType:{}", resultRawType, resultActualType);
            }
            if (tryc) {
                try {
                    return getJdbcTemplateSql(jdbcTemplate,
                            resultRawType, resolverSql, resultActualType, args);
                } catch (Exception e) {
                    LOG.error("主动抛出sql异常", e);
                    return null;
                }
            }else {
                return getJdbcTemplateSql(jdbcTemplate,
                        resultRawType, resolverSql, resultActualType, args);
            }
        }
        return null;
    }
}
