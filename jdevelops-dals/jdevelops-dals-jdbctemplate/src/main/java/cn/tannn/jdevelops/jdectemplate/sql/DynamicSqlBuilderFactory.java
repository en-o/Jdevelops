package cn.tannn.jdevelops.jdectemplate.sql;

import cn.tannn.jdevelops.jdectemplate.annotations.*;
import cn.tannn.jdevelops.jdectemplate.enums.QueryType;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

import static cn.tannn.jdevelops.jdectemplate.sql.SqlUtil.camelToSnake;
import static cn.tannn.jdevelops.jdectemplate.sql.v2.SqlConditionProcessor.processQueryConditions;
import static cn.tannn.jdevelops.jdectemplate.sql.v2.SqlFieldBuilder.buildSelectFields;
import static cn.tannn.jdevelops.jdectemplate.sql.v2.SqlOrderProcessor.processOrderBy;
import static cn.tannn.jdevelops.jdectemplate.sql.v2.SqlPageProcessor.processPage;

/**
 * 动态SQL构建器工厂 - 基于注解自动构建查询条件
 */
public class DynamicSqlBuilderFactory {

    /**
     * 使用默认命名参数模式构建
     */
    public static DynamicSqlBuilder buildJdbc(Object queryObj, String customSelect) {
        return buildJdbc(queryObj,null,customSelect, ParameterMode.NAMED);
    }

    /**
     * 使用默认命名参数模式构建
     */
    public static DynamicSqlBuilder buildJdbc(Object queryObj, Class<?> returnClazz) {
        return buildJdbc(queryObj,returnClazz,null, ParameterMode.NAMED);
    }

    /**
     * 使用默认命名参数模式构建
     * @param queryObj 查询对象
     * @param extraWhere @param extraWhere 扩展查询参数， 若为 null 则忽略；否则接收 (builder, extraMap) 由调用方自行处理
     */
    public static DynamicSqlBuilder buildJdbc(Object queryObj,
                                              String customSelect,
                                              Consumer<DynamicSqlBuilder> extraWhere) {
        return buildJdbc(queryObj,null,customSelect, ParameterMode.NAMED, extraWhere);
    }
    /**
     * 使用默认命名参数模式构建
     * @param queryObj 查询对象
     * @param extraWhere @param extraWhere 扩展查询参数， 若为 null 则忽略；否则接收 (builder, extraMap) 由调用方自行处理
     */
    public static DynamicSqlBuilder buildJdbc(Object queryObj,
                                              Class<?> returnClazz,
                                              Consumer<DynamicSqlBuilder> extraWhere) {
        return buildJdbc(queryObj,returnClazz,null, ParameterMode.NAMED, extraWhere);
    }

    /**
     * 允许调用方完全自定义 “额外参数” 如何注入 builder。
     *
     * @param queryObj   原查询对象
     * @param mode       参数模式
     * @param extraWhere 扩展参数， 若为 null 则忽略；否则接收 (builder, extraMap) 由调用方自行处理
     */
    public static DynamicSqlBuilder buildJdbc(Object queryObj,
                                              Class<?> returnClazz, String customSelect,
                                              ParameterMode mode,
                                              Consumer<DynamicSqlBuilder> extraWhere) {
        if (queryObj == null) {
            throw new IllegalArgumentException("Query object cannot be null");
        }

        Class<?> clazz = queryObj.getClass();
        String baseSql = buildBaseSql(clazz,returnClazz,customSelect);
        DynamicSqlBuilder builder = new DynamicSqlBuilder(baseSql, mode);


        // 查询条件
        processQueryConditions(builder, queryObj);
        // 扩展参数查询条件
        if (extraWhere != null) {
            extraWhere.accept(builder);
        }

        //  排序 & 分页
        processOrderBy(builder, clazz);
        processPage(builder, queryObj);

        return builder;
    }


    /**
     * 根据查询对象构建DynamicSqlBuilder
     *
     * @param queryObj 查询对象
     * @param mode     参数模式
     * @return DynamicSqlBuilder实例
     */
    public static DynamicSqlBuilder buildJdbc(Object queryObj,
                                              Class<?> returnClazz, String customSelect,
                                              ParameterMode mode) {
        if (queryObj == null) {
            throw new IllegalArgumentException("Query object cannot be null");
        }

        Class<?> clazz = queryObj.getClass();

        // 1. 构建基础SQL
        String baseSql = buildBaseSql(clazz,returnClazz,customSelect);
        DynamicSqlBuilder builder = new DynamicSqlBuilder(baseSql, mode);

        // 2. 处理查询条件
        processQueryConditions(builder, queryObj);

        // 3. 处理排序
        processOrderBy(builder, clazz);

        // 4. 处理分页
        processPage(builder, queryObj);

        return builder;
    }


    /**
     * 构建基础SQL
     */
    private static String buildBaseSql(Class<?> queryClazz, Class<?> returnClazz, String customSelect) {
        SqlTable tableAnnotation = queryClazz.getAnnotation(SqlTable.class);
        if (tableAnnotation == null) {
            throw new IllegalArgumentException("queryClazz must be annotated with @SqlTable");
        }

        StringBuilder sql = new StringBuilder("SELECT ");


        // 构建SELECT字段
        sql.append(buildSelectFields(returnClazz, customSelect));

        // 构建FROM子句
        sql.append(" FROM ").append(tableAnnotation.name());
        if (StringUtils.hasText(tableAnnotation.alias())) {
            sql.append(" ").append(tableAnnotation.alias());
        }

        // 构建JOIN子句
        for (SqlJoin join : tableAnnotation.joins()) {
            sql.append(" ").append(join.type().getSql())
                    .append(" ").append(join.table())
                    .append(" ").append(join.alias())
                    .append(" ON ").append(join.condition());
        }

        return sql.toString();
    }

}
