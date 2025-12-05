package cn.tannn.jdevelops.jdectemplate.sql.v2;

import cn.tannn.jdevelops.annotations.jdbctemplate.sql.SqlColumn;
import cn.tannn.jdevelops.annotations.jdbctemplate.sql.SqlPage;
import cn.tannn.jdevelops.annotations.jdbctemplate.sql.enums.NullHandleStrategy;
import cn.tannn.jdevelops.annotations.jdbctemplate.sql.enums.QueryType;
import cn.tannn.jdevelops.jdectemplate.sql.DynamicSqlBuilder;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static cn.tannn.jdevelops.jdectemplate.sql.SqlUtil.camelToSnake;

/**
 * WHERE条件处理
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/9/25 09:55
 */
public class SqlConditionProcessor {

    /**
     * 处理查询条件
     */
    public static void processQueryConditions(DynamicSqlBuilder builder, Object queryObj) {
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
                    builder.between(columnName, paramName, split[0], paramName, split[split.length - 1]);
                } else {
                    builder.between(columnName, split[0], split[split.length - 1]);
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

}
