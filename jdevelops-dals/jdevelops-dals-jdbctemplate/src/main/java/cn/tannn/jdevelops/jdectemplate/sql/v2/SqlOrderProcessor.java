package cn.tannn.jdevelops.jdectemplate.sql.v2;

import cn.tannn.jdevelops.annotations.jdbctemplate.sql.SqlOrderBy;
import cn.tannn.jdevelops.jdectemplate.sql.DynamicSqlBuilder;
import org.springframework.util.StringUtils;

/**
 *  ORDER BY处理
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/9/25 09:57
 */
public class SqlOrderProcessor {

    /**
     * 处理排序
     */
    public static void processOrderBy(DynamicSqlBuilder builder, Class<?> clazz) {
        SqlOrderBy orderByAnnotation = clazz.getAnnotation(SqlOrderBy.class);
        if (orderByAnnotation != null && StringUtils.hasText(orderByAnnotation.value())) {
            builder.orderBy(orderByAnnotation.value());
        }
    }

}
