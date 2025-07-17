package cn.tannn.jdevelops.jdectemplate.sql;

import cn.tannn.jdevelops.jdectemplate.annotations.*;
import cn.tannn.jdevelops.jdectemplate.enums.NullHandleStrategy;
import cn.tannn.jdevelops.jdectemplate.enums.QueryType;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 动态SQL构建器工厂 - 基于注解自动构建查询条件
 *
 */
public class DynamicSqlBuilderFactory {

    /**
     * 根据查询对象构建DynamicSqlBuilder
     *
     * @param queryObj 查询对象
     * @param mode 参数模式
     * @return DynamicSqlBuilder实例
     */
    public static DynamicSqlBuilder buildJdbc(Object queryObj, ParameterMode mode) {
        if (queryObj == null) {
            throw new IllegalArgumentException("Query object cannot be null");
        }

        Class<?> clazz = queryObj.getClass();

        // 1. 构建基础SQL
        String baseSql = buildBaseSql(clazz);
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
     * 使用默认命名参数模式构建
     */
    public static DynamicSqlBuilder buildJdbc(Object queryObj) {
        return buildJdbc(queryObj, ParameterMode.NAMED);
    }

    /**
     * 构建基础SQL
     */
    private static String buildBaseSql(Class<?> clazz) {
        SqlTable tableAnnotation = clazz.getAnnotation(SqlTable.class);
        if (tableAnnotation == null) {
            throw new IllegalArgumentException("Class must be annotated with @SqlTable");
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");

        // 构建SELECT字段
        sql.append(buildSelectFields(clazz, tableAnnotation));

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

    /**
     * 构建SELECT字段
     */
    private static String buildSelectFields(Class<?> clazz, SqlTable tableAnnotation) {
        StringBuilder fields = new StringBuilder();
        Field[] allFields = clazz.getDeclaredFields();
        List<String> fieldsList = new ArrayList<>();

        for (Field field : allFields) {
            // 跳过分页相关字段
            if (field.isAnnotationPresent(SqlPage.class)) {
                continue;
            }

            SqlColumn columnAnnotation = field.getAnnotation(SqlColumn.class);
            String tableAlias = StringUtils.hasText(tableAnnotation.alias()) ?
                              tableAnnotation.alias() : "";

            if (columnAnnotation != null) {
                String columnName = StringUtils.hasText(columnAnnotation.name()) ?
                                  columnAnnotation.name() : camelToSnake(field.getName());
                String fullColumnName = StringUtils.hasText(columnAnnotation.tableAlias()) ?
                                      columnAnnotation.tableAlias() + "." + columnName :
                                      (StringUtils.hasText(tableAlias) ? tableAlias + "." + columnName : columnName);

                fieldsList.add(fullColumnName);
            } else {
                // 默认使用驼峰转下划线
                String columnName = camelToSnake(field.getName());
                String fullColumnName = StringUtils.hasText(tableAlias) ?
                                      tableAlias + "." + columnName : columnName;
                fieldsList.add(fullColumnName);
            }
        }

        return fieldsList.isEmpty() ? "*" : String.join(", ", fieldsList);
    }

    /**
     * 处理查询条件
     */
    private static void processQueryConditions(DynamicSqlBuilder builder, Object queryObj) {
        Class<?> clazz = queryObj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            // 跳过分页字段
            if (field.isAnnotationPresent(SqlPage.class)) {
                continue;
            }

            try {
                Object value = field.get(queryObj);
                SqlColumn columnAnnotation = field.getAnnotation(SqlColumn.class);

                // 如果字段标记为不参与查询，则跳过
                if (columnAnnotation != null && !columnAnnotation.queryable()) {
                    continue;
                }

                processFieldCondition(builder, field, value, columnAnnotation);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error accessing field: " + field.getName(), e);
            }
        }
    }

    /**
     * 处理单个字段条件
     */
    private static void processFieldCondition(DynamicSqlBuilder builder, Field field, Object value, SqlColumn columnAnnotation) {
        if (value == null) {
            return;
        }

        String columnName = getColumnName(field, columnAnnotation);
        QueryType queryType = columnAnnotation != null ? columnAnnotation.queryType() : QueryType.EQ;
        NullHandleStrategy nullStrategy = columnAnnotation != null ? columnAnnotation.nullStrategy() : NullHandleStrategy.IGNORE;
        String paramName = columnAnnotation != null && StringUtils.hasText(columnAnnotation.paramName()) ?
                          columnAnnotation.paramName() : null;

        switch (queryType) {
            case EQ:
                if (paramName != null && !paramName.isEmpty()) {
                    builder.eq(columnName, paramName, value);
                } else {
                    builder.dynamicEq(columnName, value, nullStrategy);
                }
                break;
            case LIKE:
                if (paramName != null && !paramName.isEmpty()) {
                    builder.like(columnName, paramName, value.toString());
                } else {
                    builder.dynamicLike(columnName, value.toString(), nullStrategy);
                }
                break;
            case LEFT_LIKE:
                if (paramName != null && !paramName.isEmpty()) {
                    builder.leftLike(columnName, paramName, value.toString());
                } else {
                    builder.leftLike(columnName, value.toString());
                }
                break;
            case RIGHT_LIKE:
                if (paramName != null && !paramName.isEmpty()) {
                    builder.rightLike(columnName, paramName, value.toString());
                } else {
                    builder.rightLike(columnName, value.toString());
                }
                break;
            case IN:
                if (value instanceof Collection) {
                    List<?> list = new ArrayList<>((Collection<?>) value);
                    if (paramName != null && !paramName.isEmpty()) {
                        builder.in(columnName, paramName, list);
                    } else {
                        builder.in(columnName, list);
                    }
                }
                break;
            case GT:
                if (paramName != null && !paramName.isEmpty()) {
                    builder.op(columnName, ">", paramName, value);
                } else {
                    builder.op(columnName, ">", value);
                }
                break;
            case GE:
                if (paramName != null && !paramName.isEmpty()) {
                    builder.op(columnName, ">=", paramName, value);
                } else {
                    builder.op(columnName, ">=", value);
                }
                break;
            case LT:
                if (paramName != null && !paramName.isEmpty()) {
                    builder.op(columnName, "<", paramName, value);
                } else {
                    builder.op(columnName, "<", value);
                }
                break;
            case LE:
                if (paramName != null && !paramName.isEmpty()) {
                    builder.op(columnName, "<=", paramName, value);
                } else {
                    builder.op(columnName, "<=", value);
                }
                break;
            case NE:
                if (paramName != null && !paramName.isEmpty()) {
                    builder.op(columnName, "!=", paramName, value);
                } else {
                    builder.op(columnName, "!=", value);
                }
                break;
            case CUSTOM:
                String operator = columnAnnotation.operator();
                if (paramName != null && !paramName.isEmpty()) {
                    builder.op(columnName, operator, paramName, value);
                } else {
                    builder.op(columnName, operator, value);
                }
                break;
            case BETWEEN:
                String valueStr = value.toString();
                String[] split = valueStr.split(",");
                if (paramName != null && !paramName.isEmpty()) {
                    builder.between(columnName, paramName,split[0], paramName, split[split.length-1]);
                } else {
                    builder.between(columnName, split[0],split[split.length-1]);
                }
                break;
        }
    }

    /**
     * 获取列名
     */
    private static String getColumnName(Field field, SqlColumn columnAnnotation) {
        if (columnAnnotation != null) {
            String columnName = StringUtils.hasText(columnAnnotation.name()) ?
                              columnAnnotation.name() : camelToSnake(field.getName());

            if (StringUtils.hasText(columnAnnotation.tableAlias())) {
                return columnAnnotation.tableAlias() + "." + columnName;
            }
            return columnName;
        }

        return camelToSnake(field.getName());
    }

    /**
     * 处理排序
     */
    private static void processOrderBy(DynamicSqlBuilder builder, Class<?> clazz) {
        SqlOrderBy orderByAnnotation = clazz.getAnnotation(SqlOrderBy.class);
        if (orderByAnnotation != null && StringUtils.hasText(orderByAnnotation.value())) {
            builder.orderBy(orderByAnnotation.value());
        }
    }

    /**
     * 处理分页
     */
    private static void processPage(DynamicSqlBuilder builder, Object queryObj) {
        Class<?> clazz = queryObj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Integer pageIndex = null;
        Integer pageSize = null;
        Object pageInfo = null;

        for (Field field : fields) {
            field.setAccessible(true);
            SqlPage pageAnnotation = field.getAnnotation(SqlPage.class);

            if (pageAnnotation != null) {
                try {
                    Object value = field.get(queryObj);
                    switch (pageAnnotation.type()) {
                        case PAGE_INDEX:
                            pageIndex = (Integer) value;
                            break;
                        case PAGE_SIZE:
                            pageSize = (Integer) value;
                            break;
                        case PAGE_INFO:
                            pageInfo = value;
                            break;
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error accessing page field: " + field.getName(), e);
                }
            }
        }

        // 处理分页信息对象
        if (pageInfo != null) {
            try {
                Field pageIndexField = pageInfo.getClass().getDeclaredField("pageIndex");
                Field pageSizeField = pageInfo.getClass().getDeclaredField("pageSize");

                pageIndexField.setAccessible(true);
                pageSizeField.setAccessible(true);

                pageIndex = (Integer) pageIndexField.get(pageInfo);
                pageSize = (Integer) pageSizeField.get(pageInfo);
            } catch (Exception e) {
                // 忽略错误，可能字段名不匹配
            }
        }

        // 应用分页
        if (pageIndex != null && pageSize != null) {
            builder.pageZero(pageIndex, pageSize);
        }
    }

    /**
     * 驼峰转下划线
     */
    private static String camelToSnake(String camelCase) {
        return camelCase.replaceAll("([A-Z])", "_$1").toLowerCase();
    }
}
