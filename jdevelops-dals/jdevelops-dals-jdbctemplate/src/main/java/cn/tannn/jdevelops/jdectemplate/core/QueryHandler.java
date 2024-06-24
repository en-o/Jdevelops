package cn.tannn.jdevelops.jdectemplate.core;

import cn.tannn.jdevelops.jdectemplate.annotation.Query;
import cn.tannn.jdevelops.jdectemplate.util.AnnotationParse;
import cn.tannn.jdevelops.jdectemplate.util.JdbcTemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static cn.tannn.jdevelops.jdectemplate.util.JdbcTemplateUtil.*;

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

            if (ann instanceof Query) {
                Query q = (Query) ann;
                sql = q.value();
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("jdbctemplate ========> query sql : {},  args:{}", sql,  args);
            }
            if ( sql == null) {
                return null;
            }
            // 返回对象 - List
            String resultRawType;
            // 返回对象 - bean
            String resultActualType;
            Object resolver = AnnotationParse.newInstance().resolver(method, args, sql);
            if(method.getGenericReturnType() instanceof ParameterizedType){
                ParameterizedType genericReturnType = (ParameterizedType) method.getGenericReturnType();
                 resultRawType = genericReturnType.getRawType().getTypeName();
                 resultActualType = genericReturnType.getActualTypeArguments()[0].getTypeName();
            }else {
                resultRawType = method.getGenericReturnType().getTypeName();
                resultActualType = resultRawType;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("jdbctemplate ========> resultRawType : {},  resultActualType:{}", resultRawType,  resultActualType);
            }
            return getJdbcTemplateSql(jdbcTemplate,
                    resultRawType, resolver, resultActualType);
        }
        return null;
    }
}
