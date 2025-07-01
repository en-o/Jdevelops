package cn.tannn.jdevelops.jdectemplate.sql;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 灵活的动态SQL构建工具类
 * 支持位置参数和命名参数两种模式
 */
public class DynamicSqlBuilder extends OrGroupSqlBuilder {

    /**
     * 构造函数 - 位置参数模式
     * @param baseSql 基础SQL语句
     */
    public DynamicSqlBuilder(String baseSql) {
        this(baseSql, ParameterMode.POSITIONAL);
    }

    /**
     * 构造函数 - 指定参数模式
     * @param baseSql 基础SQL语句
     * @param mode 参数模式（POSITIONAL或NAMED）
     */
    public DynamicSqlBuilder(String baseSql, ParameterMode mode) {
        super(baseSql, mode);
    }

    /**
     * 添加等值条件
     * @param column 列名
     * @param value 条件值
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder eq(String column, Object value) {
        if (!isValidColumn(column) || !isValidValue(value)) {
            return this;
        }

        appendWhereOrAnd();
        if (mode == ParameterMode.POSITIONAL) {
            sql.append(column).append(" = ?");
            positionalParams.add(value);
        } else {
            String paramName = generateParamName(column);
            sql.append(column).append(" = :").append(paramName);
            namedParams.addValue(paramName, value);
        }

        if (inOrGroup) {
            orGroupHasConditions = true;
        }
        return this;
    }

    /**
     * 添加等值条件 - 命名参数模式下可指定参数名
     * @param column 列名
     * @param paramName 参数名（仅命名参数模式有效）
     * @param value 条件值
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder eq(String column, String paramName, Object value) {
        if (!isValidColumn(column) || !isValidValue(value)) {
            return this;
        }

        if (mode == ParameterMode.NAMED) {
            if (!isValidParamName(paramName)) {
                throw new IllegalArgumentException("Parameter name cannot be null or empty in named mode");
            }
            appendWhereOrAnd();
            sql.append(column).append(" = :").append(paramName);
            namedParams.addValue(paramName, value);
        } else {
            // 位置参数模式下忽略参数名，直接调用基础方法
            return eq(column, value);
        }

        if (inOrGroup) {
            orGroupHasConditions = true;
        }
        return this;
    }

    /**
     * 添加自定义操作符条件（自动参数名）
     * @param column 列名
     * @param operator 操作符（如">", "<="等）
     * @param value 条件值
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder op(String column, String operator, Object value) {
        if (!isValidColumn(column) || !isValidOperator(operator) || !isValidValue(value)) {
            return this;
        }

        appendWhereOrAnd();
        if (mode == ParameterMode.POSITIONAL) {
            sql.append(column).append(" ").append(operator).append(" ?");
            positionalParams.add(value);
        } else {
            String paramName = generateParamName(column);
            sql.append(column).append(" ").append(operator).append(" :").append(paramName);
            namedParams.addValue(paramName, value);
        }

        if (inOrGroup) {
            orGroupHasConditions = true;
        }
        return this;
    }

    /**
     * 添加自定义操作符条件（指定参数名，仅命名参数模式）
     * @param column 列名
     * @param operator 操作符（如">", "<="等）
     * @param paramName 参数名
     * @param value 条件值
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder op(String column, String operator, String paramName, Object value) {
        if (!isValidColumn(column) || !isValidOperator(operator) || !isValidValue(value)) {
            return this;
        }

        if (mode == ParameterMode.NAMED) {
            if (!isValidParamName(paramName)) {
                throw new IllegalArgumentException("Parameter name cannot be null or empty in named mode");
            }
            appendWhereOrAnd();
            sql.append(column).append(" ").append(operator).append(" :").append(paramName);
            namedParams.addValue(paramName, value);
        } else {
            return op(column, operator, value);
        }

        if (inOrGroup) {
            orGroupHasConditions = true;
        }
        return this;
    }

    /**
     * 添加LIKE条件
     * @param column 列名
     * @param value LIKE条件值（自动添加%通配符）
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder like(String column, String value) {
        if (!isValidColumn(column) || !isValidValue(value)) {
            return this;
        }

        appendWhereOrAnd();
        String likeValue = "%" + value + "%";
        if (mode == ParameterMode.POSITIONAL) {
            sql.append(column).append(" LIKE ?");
            positionalParams.add(likeValue);
        } else {
            String paramName = generateParamName(column + "Like");
            sql.append(column).append(" LIKE :").append(paramName);
            namedParams.addValue(paramName, likeValue);
        }

        if (inOrGroup) {
            orGroupHasConditions = true;
        }
        return this;
    }

    /**
     * 添加LIKE条件 - 命名参数模式下可指定参数名
     * @param column 列名
     * @param paramName 参数名
     * @param value LIKE条件值（自动添加%通配符）
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder like(String column, String paramName, String value) {
        if (!isValidColumn(column) || !isValidValue(value)) {
            return this;
        }

        if (mode == ParameterMode.NAMED) {
            if (!isValidParamName(paramName)) {
                throw new IllegalArgumentException("Parameter name cannot be null or empty in named mode");
            }
            appendWhereOrAnd();
            sql.append(column).append(" LIKE :").append(paramName);
            namedParams.addValue(paramName, "%" + value + "%");
        } else {
            return like(column, value);
        }

        if (inOrGroup) {
            orGroupHasConditions = true;
        }
        return this;
    }

    /**
     * 添加IN条件
     * @param column 列名
     * @param values IN条件值列表
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addInCondition(String column, List<?> values) {
        if (!isValidColumn(column) || values == null || values.isEmpty()) {
            return this;
        }

        appendWhereOrAnd();
        if (mode == ParameterMode.POSITIONAL) {
            sql.append(column).append(" IN (");
            for (int i = 0; i < values.size(); i++) {
                if (i > 0) sql.append(", ");
                sql.append("?");
                positionalParams.add(values.get(i));
            }
            sql.append(")");
        } else {
            String paramName = generateParamName(column + "List");
            sql.append(column).append(" IN (:").append(paramName).append(")");
            namedParams.addValue(paramName, values);
        }

        if (inOrGroup) {
            orGroupHasConditions = true;
        }
        return this;
    }

    /**
     * 添加IN条件 - 命名参数模式下可指定参数名
     * @param column 列名
     * @param paramName 参数名
     * @param values IN条件值列表
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addInCondition(String column, String paramName, List<?> values) {
        if (!isValidColumn(column) || values == null || values.isEmpty()) {
            return this;
        }

        if (mode == ParameterMode.NAMED) {
            if (!isValidParamName(paramName)) {
                throw new IllegalArgumentException("Parameter name cannot be null or empty in named mode");
            }
            appendWhereOrAnd();
            sql.append(column).append(" IN (:").append(paramName).append(")");
            namedParams.addValue(paramName, values);
        } else {
            return addInCondition(column, values);
        }

        if (inOrGroup) {
            orGroupHasConditions = true;
        }
        return this;
    }

    /**
     * 添加BETWEEN条件
     * @param column 列名
     * @param startValue 范围开始值
     * @param endValue 范围结束值
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addBetweenCondition(String column, Object startValue, Object endValue) {
        if (!isValidColumn(column)) {
            return this;
        }

        if (startValue != null && endValue != null) {
            appendWhereOrAnd();
            if (mode == ParameterMode.POSITIONAL) {
                sql.append(column).append(" BETWEEN ? AND ?");
                positionalParams.add(startValue);
                positionalParams.add(endValue);
            } else {
                String startParamName = generateParamName(column + "Start");
                String endParamName = generateParamName(column + "End");
                sql.append(column).append(" BETWEEN :").append(startParamName)
                   .append(" AND :").append(endParamName);
                namedParams.addValue(startParamName, startValue);
                namedParams.addValue(endParamName, endValue);
            }
            if (inOrGroup) {
                orGroupHasConditions = true;
            }
        } else if (startValue != null) {
            return op(column, ">=", startValue);
        } else if (endValue != null) {
            return op(column, "<=", endValue);
        }
        return this;
    }

    /**
     * 添加BETWEEN条件 - 命名参数模式下可指定参数名
     * @param column 列名
     * @param startParamName 开始值参数名
     * @param startValue 范围开始值
     * @param endParamName 结束值参数名
     * @param endValue 范围结束值
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addBetweenCondition(String column, String startParamName, Object startValue,
                                                String endParamName, Object endValue) {
        if (!isValidColumn(column)) {
            return this;
        }

        if (mode == ParameterMode.NAMED) {
            if (startValue != null && endValue != null) {
                if (!isValidParamName(startParamName) || !isValidParamName(endParamName)) {
                    throw new IllegalArgumentException("Parameter names cannot be null or empty in named mode");
                }
                appendWhereOrAnd();
                sql.append(column).append(" BETWEEN :").append(startParamName)
                   .append(" AND :").append(endParamName);
                namedParams.addValue(startParamName, startValue);
                namedParams.addValue(endParamName, endValue);
                if (inOrGroup) {
                    orGroupHasConditions = true;
                }
            } else if (startValue != null) {
                if (!isValidParamName(startParamName)) {
                    throw new IllegalArgumentException("Start parameter name cannot be null or empty in named mode");
                }
                return op(column, ">=", startParamName, startValue);
            } else if (endValue != null) {
                if (!isValidParamName(endParamName)) {
                    throw new IllegalArgumentException("End parameter name cannot be null or empty in named mode");
                }
                return op(column, "<=", endParamName, endValue);
            }
        } else {
            return addBetweenCondition(column, startValue, endValue);
        }
        return this;
    }

    /**
     * 添加IS NULL条件
     * @param column 列名
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addIsNullCondition(String column) {
        if (!isValidColumn(column)) {
            return this;
        }

        appendWhereOrAnd();
        sql.append(column).append(" IS NULL");
        if (inOrGroup) {
            orGroupHasConditions = true;
        }
        return this;
    }

    /**
     * 添加IS NOT NULL条件
     * @param column 列名
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addIsNotNullCondition(String column) {
        if (!isValidColumn(column)) {
            return this;
        }

        appendWhereOrAnd();
        sql.append(column).append(" IS NOT NULL");
        if (inOrGroup) {
            orGroupHasConditions = true;
        }
        return this;
    }

    /**
     * 添加OR条件组（括号包围）
     * @param builders 包含多个条件的DynamicSqlBuilder数组
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addOrConditionGroup(DynamicSqlBuilder... builders) {
        if (builders == null || builders.length == 0) {
            return this;
        }

        List<DynamicSqlBuilder> validBuilders = new ArrayList<>();
        for (DynamicSqlBuilder builder : builders) {
            if (builder != null && builder.hasConditions()) {
                validBuilders.add(builder);
            }
        }

        if (validBuilders.isEmpty()) {
            return this;
        }

        appendWhereOrAnd();
        sql.append("(");

        for (int i = 0; i < validBuilders.size(); i++) {
            if (i > 0) {
                sql.append(" OR ");
            }
            DynamicSqlBuilder builder = validBuilders.get(i);
            String builderSql = builder.getSql();

            // 提取WHERE子句
            int whereIndex = builderSql.toUpperCase().indexOf(" WHERE ");
            if (whereIndex > 0) {
                sql.append(builderSql.substring(whereIndex + 7));
            }

            // 合并参数
            if (mode == ParameterMode.POSITIONAL && builder.mode == ParameterMode.POSITIONAL) {
                List<Object> builderParams = builder.getPositionalParamsList();
                if (builderParams != null) {
                    positionalParams.addAll(builderParams);
                }
            } else if (mode == ParameterMode.NAMED && builder.mode == ParameterMode.NAMED) {
                MapSqlParameterSource builderParams = builder.getNamedParams();
                if (builderParams != null) {
                    for (String paramName : builderParams.getParameterNames()) {
                        namedParams.addValue(paramName, builderParams.getValue(paramName));
                    }
                }
            }
        }

        sql.append(")");
        return this;
    }

    /**
     * 添加ORDER BY子句
     * @param orderBy 排序字段和方向（如"id DESC"）
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addOrderBy(String orderBy) {
        if (orderBy != null && !orderBy.trim().isEmpty()) {
            sql.append(" ORDER BY ").append(orderBy);
        }
        return this;
    }

    /**
     * 添加GROUP BY子句
     * @param groupBy 分组字段
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addGroupBy(String groupBy) {
        if (groupBy != null && !groupBy.trim().isEmpty()) {
            sql.append(" GROUP BY ").append(groupBy);
        }
        return this;
    }

    /**
     * 添加HAVING子句
     * @param having HAVING条件
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addHaving(String having) {
        if (having != null && !having.trim().isEmpty()) {
            sql.append(" HAVING ").append(having);
        }
        return this;
    }

    /**
     * 添加LIMIT子句
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addLimit(Integer offset, Integer limit) {
        if (limit != null && limit > 0) {
            sql.append(" LIMIT ");
            if (mode == ParameterMode.POSITIONAL) {
                if (offset != null && offset >= 0) {
                    sql.append("?, ");
                    positionalParams.add(offset);
                }
                sql.append("?");
                positionalParams.add(limit);
            } else {
                if (offset != null && offset >= 0) {
                    sql.append(":offset, ");
                    namedParams.addValue("offset", offset);
                }
                sql.append(":limit");
                namedParams.addValue("limit", limit);
            }
        }
        return this;
    }

    /**
     * 添加分页参数
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页数量
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addPagination(Integer pageNum, Integer pageSize) {
        if (pageNum != null && pageSize != null && pageNum > 0 && pageSize > 0) {
            int offset = (pageNum - 1) * pageSize;
            return addLimit(offset, pageSize);
        }
        return this;
    }

    /**
     * 添加自定义条件（原始SQL片段）
     * @param condition 自定义SQL条件片段
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addCustomCondition(String condition) {
        if (condition != null && !condition.trim().isEmpty()) {
            appendWhereOrAnd();
            sql.append(condition);
            if (inOrGroup) {
                orGroupHasConditions = true;
            }
        }
        return this;
    }

    /**
     * 添加OR条件表达式
     * @param orExpression OR表达式的lambda函数
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder or(java.util.function.Function<DynamicSqlBuilder, DynamicSqlBuilder> orExpression) {
        if (orExpression == null) {
            return this;
        }

        DynamicSqlBuilder tempBuilder = new DynamicSqlBuilder("SELECT 1", this.mode);
        tempBuilder.paramCounter = this.paramCounter;
        tempBuilder.inOrGroup = true;
        tempBuilder.orGroupHasConditions = false;

        tempBuilder = orExpression.apply(tempBuilder);
        this.paramCounter = tempBuilder.paramCounter;

        if (!tempBuilder.hasConditions()) {
            return this;
        }

        appendWhereOrAnd();
        sql.append("(");

        String tempSql = tempBuilder.getSql();
        int whereIndex = tempSql.toUpperCase().indexOf(" WHERE ");
        if (whereIndex > 0) {
            String conditions = tempSql.substring(whereIndex + 7);
            conditions = replaceTopLevelAndWithOr(conditions);
            sql.append(conditions);
        }

        mergeParameters(tempBuilder);
        sql.append(")");
        return this;
    }

    /**
     * 添加OR等值条件（简化写法）
     * @param column 列名
     * @param value 条件值
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addOrCondition(String column, Object value) {
        if (!isValidColumn(column) || !isValidValue(value)) {
            return this;
        }

        if (inOrGroup) {
            return eq(column, value);
        }

        appendOrCondition();
        if (mode == ParameterMode.POSITIONAL) {
            sql.append(column).append(" = ?");
            positionalParams.add(value);
        } else {
            String paramName = generateParamName(column);
            sql.append(column).append(" = :").append(paramName);
            namedParams.addValue(paramName, value);
        }
        return this;
    }

    /**
     * 添加OR等值条件 - 命名参数模式下可指定参数名
     * @param column 列名
     * @param paramName 参数名
     * @param value 条件值
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addOrCondition(String column, String paramName, Object value) {
        if (!isValidColumn(column) || !isValidValue(value)) {
            return this;
        }

        if (mode == ParameterMode.NAMED) {
            if (!isValidParamName(paramName)) {
                throw new IllegalArgumentException("Parameter name cannot be null or empty in named mode");
            }
            appendOrCondition();
            sql.append(column).append(" = :").append(paramName);
            namedParams.addValue(paramName, value);
        } else {
            return addOrCondition(column, value);
        }
        return this;
    }

    /**
     * 切换到命名参数模式（如果当前是位置参数模式且没有参数）
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder switchToNamedMode() {
        if (mode == ParameterMode.POSITIONAL && (positionalParams == null || positionalParams.isEmpty())) {
            this.mode = ParameterMode.NAMED;
            this.positionalParams = null;
            this.namedParams = new MapSqlParameterSource();
        }
        return this;
    }

    /**
     * 重置条件（保留基础SQL）
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder reset() {
        String currentSql = sql.toString();
        int whereIndex = currentSql.toUpperCase().indexOf(" WHERE ");
        if (whereIndex > 0) {
            sql.setLength(0);
            sql.append(currentSql.substring(0, whereIndex));
        }

        if (mode == ParameterMode.POSITIONAL && positionalParams != null) {
            positionalParams.clear();
        } else if (mode == ParameterMode.NAMED && namedParams != null) {
            namedParams = new MapSqlParameterSource();
        }

        hasWhere = false;
        lastConditionWasOr = false;
        paramCounter = 0;
        inOrGroup = false;
        orGroupHasConditions = false;
        return this;
    }

    /**
     * 添加动态等值条件（支持空值处理策略）
     * @param column 列名
     * @param value 条件值
     * @param nullStrategy 空值处理策略
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addDynamicCondition(String column, Object value, NullHandleStrategy nullStrategy) {
        if (!isValidColumn(column)) {
            return this;
        }

        if (nullStrategy == null) {
            nullStrategy = NullHandleStrategy.IGNORE;
        }

        // 根据策略处理null和空值
        if (value == null) {
            switch (nullStrategy) {
                case NULL_AS_IS_NULL:
                case NULL_AND_EMPTY_AS_IS_NULL:
                    return addIsNullCondition(column);
                case NULL_AS_IS_NOT_NULL:
                case NULL_AND_EMPTY_AS_IS_NOT_NULL:
                    return addIsNotNullCondition(column);
                default:
                    return this;
            }
        }

        if (value instanceof String && !StringUtils.hasText((String) value)) {
            switch (nullStrategy) {
                case EMPTY_AS_IS_NULL:
                case NULL_AND_EMPTY_AS_IS_NULL:
                    return addIsNullCondition(column);
                case EMPTY_AS_IS_NOT_NULL:
                case NULL_AND_EMPTY_AS_IS_NOT_NULL:
                    return addIsNotNullCondition(column);
                default:
                    return this;
            }
        }

        // 正常添加条件
        return eq(column, value);
    }

    /**
     * 添加LEFT LIKE条件（左匹配）
     * @param column 列名
     * @param value LEFT LIKE条件值（自动添加右侧%通配符）
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addLeftLikeCondition(String column, String value) {
        if (!isValidColumn(column) || !StringUtils.hasText(value)) {
            return this;
        }

        appendWhereOrAnd();
        String likeValue = value + "%";
        if (mode == ParameterMode.POSITIONAL) {
            sql.append(column).append(" LIKE ?");
            positionalParams.add(likeValue);
        } else {
            String paramName = generateParamName(column + "LeftLike");
            sql.append(column).append(" LIKE :").append(paramName);
            namedParams.addValue(paramName, likeValue);
        }

        if (inOrGroup) {
            orGroupHasConditions = true;
        }
        return this;
    }

    /**
     * 添加LEFT LIKE条件 - 命名参数模式下可指定参数名
     * @param column 列名
     * @param paramName 参数名
     * @param value LEFT LIKE条件值（自动添加右侧%通配符）
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addLeftLikeCondition(String column, String paramName, String value) {
        if (!isValidColumn(column) || !StringUtils.hasText(value)) {
            return this;
        }

        if (mode == ParameterMode.NAMED) {
            if (!isValidParamName(paramName)) {
                throw new IllegalArgumentException("Parameter name cannot be null or empty in named mode");
            }
            appendWhereOrAnd();
            sql.append(column).append(" LIKE :").append(paramName);
            namedParams.addValue(paramName, value + "%");
        } else {
            return addLeftLikeCondition(column, value);
        }

        if (inOrGroup) {
            orGroupHasConditions = true;
        }
        return this;
    }

    /**
     * 添加RIGHT LIKE条件 - 右匹配
     * @param column 列名
     * @param value RIGHT LIKE条件值（自动添加左侧%通配符）
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addRightLikeCondition(String column, String value) {
        if (!isValidColumn(column) || !StringUtils.hasText(value)) {
            return this;
        }

        appendWhereOrAnd();
        String likeValue = "%" + value;
        if (mode == ParameterMode.POSITIONAL) {
            sql.append(column).append(" LIKE ?");
            positionalParams.add(likeValue);
        } else {
            String paramName = generateParamName(column + "RightLike");
            sql.append(column).append(" LIKE :").append(paramName);
            namedParams.addValue(paramName, likeValue);
        }

        if (inOrGroup) {
            orGroupHasConditions = true;
        }
        return this;
    }

    /**
     * 添加RIGHT LIKE条件 - 命名参数模式下可指定参数名
     * @param column 列名
     * @param paramName 参数名
     * @param value RIGHT LIKE条件值（自动添加左侧%通配符）
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addRightLikeCondition(String column, String paramName, String value) {
        if (!isValidColumn(column) || !StringUtils.hasText(value)) {
            return this;
        }

        if (mode == ParameterMode.NAMED) {
            if (!isValidParamName(paramName)) {
                throw new IllegalArgumentException("Parameter name cannot be null or empty in named mode");
            }
            appendWhereOrAnd();
            sql.append(column).append(" LIKE :").append(paramName);
            namedParams.addValue(paramName, "%" + value);
        } else {
            return addRightLikeCondition(column, value);
        }

        if (inOrGroup) {
            orGroupHasConditions = true;
        }
        return this;
    }

    /**
     * 添加动态LIKE条件（支持空值处理策略）
     * @param column 列名
     * @param value LIKE条件值
     * @param nullStrategy 空值处理策略
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addDynamicLikeCondition(String column, String value, NullHandleStrategy nullStrategy) {
        if (!isValidColumn(column)) {
            return this;
        }

        if (nullStrategy == null) {
            nullStrategy = NullHandleStrategy.IGNORE;
        }

        // 根据策略处理null和空值
        if (value == null) {
            switch (nullStrategy) {
                case NULL_AS_IS_NULL:
                case NULL_AND_EMPTY_AS_IS_NULL:
                    return addIsNullCondition(column);
                case NULL_AS_IS_NOT_NULL:
                case NULL_AND_EMPTY_AS_IS_NOT_NULL:
                    return addIsNotNullCondition(column);
                case IGNORE:
                default:
                    return this;
            }
        }

        if (!StringUtils.hasText(value)) {
            switch (nullStrategy) {
                case EMPTY_AS_IS_NULL:
                case NULL_AND_EMPTY_AS_IS_NULL:
                    return addIsNullCondition(column);
                case EMPTY_AS_IS_NOT_NULL:
                case NULL_AND_EMPTY_AS_IS_NOT_NULL:
                    return addIsNotNullCondition(column);
                case IGNORE:
                default:
                    return this;
            }
        }

        // 正常添加LIKE条件
        return like(column, value);
    }

    /**
     * 添加动态LIKE条件（默认忽略空值）
     * @param column 列名
     * @param value LIKE条件值
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addDynamicLikeCondition(String column, String value) {
        return addDynamicLikeCondition(column, value, NullHandleStrategy.IGNORE);
    }


    /**
     * 获取原生SQL（将参数值直接拼接到SQL中）
     * 注意：此方法仅用于调试和日志记录，不要用于实际执行
     * @return 完整的原生SQL语句
     */
    public String getNativeSql() {
        if (!hasConditions() && getParameterCount() == 0) {
            return getSql();
        }

        String sqlString = getSql();

        if (mode == ParameterMode.POSITIONAL) {
            // 处理位置参数
            for (Object param : getPositionalParams()) {
                sqlString = replaceFirstPlaceholder(sqlString, param);
            }
        } else {
            // 处理命名参数
            MapSqlParameterSource params = getNamedParams();
            for (String paramName : params.getParameterNames()) {
                Object value = params.getValue(paramName);
                sqlString = sqlString.replace(":" + paramName, formatValue(value));
            }
        }

        return sqlString;
    }

    /**
     * 替换第一个问号占位符
     * @param sql SQL语句
     * @param value 参数值
     * @return 替换后的SQL语句
     */
    private String replaceFirstPlaceholder(String sql, Object value) {
        int index = sql.indexOf('?');
        if (index == -1) {
            return sql;
        }
        return sql.substring(0, index) + formatValue(value) + sql.substring(index + 1);
    }

    /**
     * 格式化参数值
     * @param value 参数值
     * @return 格式化后的字符串
     */
    private String formatValue(Object value) {
        if (value == null) {
            return "NULL";
        }

        // 处理不同类型的值
        if (value instanceof String) {
            return "'" + escapeString((String) value) + "'";
        } else if (value instanceof java.util.Date) {
            return "'" + formatDate((java.util.Date) value) + "'";
        } else if (value instanceof java.time.temporal.Temporal) {
            return "'" + value + "'";
        } else if (value instanceof Boolean) {
            return ((Boolean) value) ? "1" : "0";
        } else if (value instanceof Collection<?>) {
            return formatCollection((Collection<?>) value);
        } else if (value instanceof Object[]) {
            return formatCollection(Arrays.asList((Object[]) value));
        } else {
            return value.toString();
        }
    }

    /**
     * 转义字符串中的特殊字符
     * @param str 原始字符串
     * @return 转义后的字符串
     */
    private String escapeString(String str) {
        return str.replace("'", "''")
                .replace("\\", "\\\\");
    }

    /**
     * 格式化日期对象
     * @param date 日期对象
     * @return 格式化的日期字符串
     */
    private String formatDate(java.util.Date date) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 格式化集合
     * @param collection 集合对象
     * @return 格式化的字符串
     */
    private String formatCollection(Collection<?> collection) {
        if (collection.isEmpty()) {
            return "()";
        }
        StringBuilder sb = new StringBuilder("(");
        boolean first = true;
        for (Object item : collection) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(formatValue(item));
            first = false;
        }
        sb.append(")");
        return sb.toString();
    }
}
