package cn.tannn.jdevelops.jdectemplate.core;

import cn.tannn.jdevelops.annotations.jdbctemplate.Query;
import cn.tannn.jdevelops.jdectemplate.util.AnnotationParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import static cn.tannn.jdevelops.jdectemplate.util.InteriorJdbcTemplateUtil.getJdbcTemplateSql;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/12/16 10:12
 */
public class ProxyCoreFun {
    private static final Logger LOG = LoggerFactory.getLogger(ProxyCoreFun.class);

    /**
     * query 查询的具体实现
     * @param query 查询注解
     * @param jdbcTemplate  {@link JdbcTemplate}
     * @param method {@link Method}
     * @param args 方法参数
     * @return 代理类
     * @throws ClassNotFoundException
     */
    public static Object query(Query query
            , JdbcTemplate jdbcTemplate
            , Method method
            , Object[] args) throws ClassNotFoundException {
        String sql = query.value();
        boolean tryc = query.tryc();
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
        } else {
            return getJdbcTemplateSql(jdbcTemplate,
                    resultRawType, resolverSql, resultActualType, args);
        }
    }
}
