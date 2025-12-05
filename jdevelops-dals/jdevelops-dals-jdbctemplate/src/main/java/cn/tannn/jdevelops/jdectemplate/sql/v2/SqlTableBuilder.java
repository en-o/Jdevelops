package cn.tannn.jdevelops.jdectemplate.sql.v2;

import cn.tannn.jdevelops.annotations.jdbctemplate.sql.SqlJoin;
import cn.tannn.jdevelops.annotations.jdbctemplate.sql.SqlTable;
import org.springframework.util.StringUtils;

/**
 * 表和JOIN构建器[ FROM/JOIN子句构建]
 */
public class SqlTableBuilder {

    /**
     * 构建FROM子句（包含JOIN）
     */
    public static String buildFromClause(Class<?> queryClazz) {
        SqlTable tableAnnotation = queryClazz.getAnnotation(SqlTable.class);
        if (tableAnnotation == null) {
            throw new IllegalArgumentException("queryClazz must be annotated with @SqlTable");
        }

        StringBuilder fromBuilder = new StringBuilder();

        // 构建FROM子句
        fromBuilder.append("FROM ").append(tableAnnotation.name());
        if (StringUtils.hasText(tableAnnotation.alias())) {
            fromBuilder.append(" ").append(tableAnnotation.alias());
        }

        // 构建JOIN子句
        for (SqlJoin join : tableAnnotation.joins()) {
            fromBuilder.append(" ").append(join.type().getSql())
                    .append(" ").append(join.table())
                    .append(" ").append(join.alias())
                    .append(" ON ").append(join.condition());
        }

        return fromBuilder.toString();
    }

    /**
     * 获取主表信息
     */
    public static String getMainTable(Class<?> queryClazz) {
        SqlTable tableAnnotation = queryClazz.getAnnotation(SqlTable.class);
        if (tableAnnotation == null) {
            throw new IllegalArgumentException("queryClazz must be annotated with @SqlTable");
        }
        return tableAnnotation.name();
    }

    /**
     * 获取主表别名
     */
    public static String getMainTableAlias(Class<?> queryClazz) {
        SqlTable tableAnnotation = queryClazz.getAnnotation(SqlTable.class);
        if (tableAnnotation == null) {
            return "";
        }
        return StringUtils.hasText(tableAnnotation.alias()) ? tableAnnotation.alias() : "";
    }

    /**
     * 构建简单的FROM子句（仅主表，不包含JOIN）
     */
    public static String buildSimpleFromClause(Class<?> queryClazz) {
        SqlTable tableAnnotation = queryClazz.getAnnotation(SqlTable.class);
        if (tableAnnotation == null) {
            throw new IllegalArgumentException("queryClazz must be annotated with @SqlTable");
        }

        StringBuilder fromBuilder = new StringBuilder();
        fromBuilder.append("FROM ").append(tableAnnotation.name());
        if (StringUtils.hasText(tableAnnotation.alias())) {
            fromBuilder.append(" ").append(tableAnnotation.alias());
        }
        return fromBuilder.toString();
    }
}
