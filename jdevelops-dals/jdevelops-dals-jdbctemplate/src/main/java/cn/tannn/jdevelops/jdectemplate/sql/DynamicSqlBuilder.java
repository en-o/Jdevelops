package cn.tannn.jdevelops.jdectemplate.sql;

import cn.tannn.jdevelops.annotations.jdbctemplate.sql.enums.NullHandleStrategy;
import cn.tannn.jdevelops.annotations.jdbctemplate.sql.enums.ParameterMode;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 灵活的动态SQL构建工具类
 * <p> 支持位置参数和命名参数两种模式
 * <p>  sql 一定要顺序构建，比如 builder.orderBy()之后才是 builder.pageZero()，要不然会报错。我是顺序组装的（我好像解决了这个问题）
 * <pre> sql书写顺序
     SELECT [DISTINCT] 列名或表达式
     FROM 表名
     [JOIN 表名 ON 连接条件]
     [WHERE 条件]
     [GROUP BY 分组列]
     [HAVING 分组后条件]
     [ORDER BY 排序列 [ASC|DESC]]
     [LIMIT/OFFSET 分页限制]
 * </pre>
 * <pre>
 * List<User> result = namedParameterJdbcTemplate.query(builder.getSql(),builder.getNamedParams(), new DataClassRowMapper<>(User.class));
 * </pre>
 * <pre>
 * List<DynamicSqlBuilderSpringTest.User> result = jdbcTemplate.query(builder.getSql(),builder.getPositionalParams(),new DataClassRowMapper<>(DynamicSqlBuilderSpringTest.User.class));
 * </pre>
 */
public class DynamicSqlBuilder extends OrGroupSqlBuilder {

    /**
     * 构造函数 - 位置参数模式
     *
     * @param baseSql 基础SQL语句
     */
    public DynamicSqlBuilder(String baseSql) {
        this(baseSql, ParameterMode.POSITIONAL);
    }

    /**
     * 构造函数 - 指定参数模式
     *
     * @param baseSql 基础SQL语句
     * @param mode    参数模式（POSITIONAL或NAMED）
     */
    public DynamicSqlBuilder(String baseSql, ParameterMode mode) {
        super(baseSql, mode);
    }

    /**
     * 添加等值条件
     *
     * @param column 列名
     * @param value  条件值
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
     *
     * @param column    列名
     * @param paramName 参数名（仅命名参数模式有效）
     * @param value     条件值
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
     *
     * @param column   列名
     * @param operator 操作符（如">", "<="等）
     * @param value    条件值
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
     *
     * @param column    列名
     * @param operator  操作符（如">", "<="等）
     * @param paramName 参数名
     * @param value     条件值
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
     *
     * @param column 列名
     * @param value  LIKE条件值（自动添加%通配符）
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
     *
     * @param column    列名
     * @param paramName 参数名
     * @param value     LIKE条件值（自动添加%通配符）
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
     *
     * @param column 列名
     * @param values IN条件值列表
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder in(String column, List<?> values) {
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
     *
     * @param column    列名
     * @param paramName 参数名
     * @param values    IN条件值列表
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder in(String column, String paramName, List<?> values) {
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
            return in(column, values);
        }

        if (inOrGroup) {
            orGroupHasConditions = true;
        }
        return this;
    }

    /**
     * 添加BETWEEN条件
     *
     * @param column     列名
     * @param startValue 范围开始值
     * @param endValue   范围结束值
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder between(String column, Object startValue, Object endValue) {
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
     *
     * @param column         列名
     * @param startParamName 开始值参数名
     * @param startValue     范围开始值
     * @param endParamName   结束值参数名
     * @param endValue       范围结束值
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder between(String column, String startParamName, Object startValue,
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
            return between(column, startValue, endValue);
        }
        return this;
    }

    /**
     * 添加IS NULL条件
     *
     * @param column 列名
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder isNull(String column) {
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
     *
     * @param column 列名
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder isNotNull(String column) {
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
     *
     * @param builders 包含多个条件的DynamicSqlBuilder数组
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder orGroup(DynamicSqlBuilder... builders) {
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
     * 改进版本：正确处理包含窗口函数、子查询的复杂SQL
     *
     * @param orderBy 排序字段和方向（如"id DESC"）
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder orderBy(String orderBy) {
        if (orderBy == null || orderBy.trim().isEmpty()) {
            return this;
        }

        String trimmedOrderBy = orderBy.trim();
        String currentSql = sql.toString();

        // 使用改进的方法查找主查询的关键位置
        int limitIndex = SqlUtil.findLimitIndex(currentSql);
        int orderByIndex = SqlUtil.findOrderByIndex(currentSql);

        if (limitIndex != -1) {
            // 存在LIMIT子句，需要在LIMIT之前插入ORDER BY
            handleOrderByWithLimit(currentSql, trimmedOrderBy, limitIndex, orderByIndex);
        } else if (orderByIndex != -1) {
            // 存在ORDER BY但无LIMIT，直接追加
            sql.append(", ").append(trimmedOrderBy);
        } else {
            // 不存在ORDER BY，添加新的ORDER BY子句
            sql.append(" ORDER BY ").append(trimmedOrderBy);
        }

        return this;
    }

    /**
     * 添加多个ORDER BY子句
     *
     * @param orderBys 排序字段数组
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder orderBy(String... orderBys) {
        if (orderBys == null || orderBys.length == 0) {
            return this;
        }

        // 过滤空值并连接
        String orderByClause = Arrays.stream(orderBys)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(", "));

        if (!orderByClause.isEmpty()) {
            return orderBy(orderByClause);
        }

        return this;
    }

    /**
     * 便捷的排序方法，支持字段名和排序方向
     *
     * @param column 排序字段
     * @param direction 排序方向（ASC/DESC）
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder orderBy(String column, String direction) {
        if (column == null || column.trim().isEmpty()) {
            return this;
        }

        String dir = (direction == null || direction.trim().isEmpty()) ? "ASC" : direction.trim();
        String orderByClause = column.trim() + " " + dir;
        return orderBy(orderByClause);
    }

    /**
     * 处理存在LIMIT子句的情况
     */
    private void handleOrderByWithLimit(String currentSql, String orderBy, int limitIndex, int orderByIndex) {
        // 提取LIMIT子句
        String limitClause = currentSql.substring(limitIndex);

        // 临时移除LIMIT子句
        sql.setLength(limitIndex);

        if (orderByIndex != -1 && orderByIndex < limitIndex) {
            // ORDER BY存在且在LIMIT之前，追加到ORDER BY
            sql.append(", ").append(orderBy);
        } else {
            // ORDER BY不存在，添加新的ORDER BY
            sql.append(" ORDER BY ").append(orderBy);
        }

        // 重新添加LIMIT子句
        sql.append(limitClause);
    }

    /**
     * 添加GROUP BY子句
     *
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
     *
     * @param having HAVING条件
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder having(String having) {
        if (having != null && !having.trim().isEmpty()) {
            sql.append(" HAVING ").append(having);
        }
        return this;
    }

    /**
     * 添加LIMIT子句
     *
     * @param offset 偏移量
     * @param limit  限制数量
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder limit(Integer offset, Integer limit) {
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
     * 添加分页参数 （注意pageNum）
     *
     * @param pageNum  页码（从0开始）
     * @param pageSize 每页数量
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder pageZero(Integer pageNum, Integer pageSize) {
        if (pageNum != null && pageSize != null && pageNum >= 0 && pageSize > 0) {
            int offset = pageNum * pageSize;
            return limit(offset, pageSize);
        }
        return this;
    }


    /**
     * 添加分页参数
     *
     * @param pageNum  页码（从1开始）
     * @param pageSize 每页数量
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder page(Integer pageNum, Integer pageSize) {
        if (pageNum != null && pageSize != null && pageNum > 0 && pageSize > 0) {
            int offset = (pageNum - 1) * pageSize;
            return limit(offset, pageSize);
        }
        return this;
    }

    /**
     * 添加自定义条件（原始SQL片段）
     *
     * @param condition 自定义SQL条件片段
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder custom(String condition) {
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
     * 添加命名参数
     *
     * @param name  参数名
     * @param value 参数值
     * @return 当前对象
     */
    public DynamicSqlBuilder addNamedParam(String name, Object value) {
        if (mode != ParameterMode.NAMED) {
            throw new IllegalStateException("Can only add named parameters in NAMED mode");
        }
        if (!isValidParamName(name)) {
            throw new IllegalArgumentException("Parameter name cannot be null or empty");
        }
        namedParams.addValue(name, value);
        return this;
    }

    /**
     * 添加位置参数 注意一定要紧跟需要设置值的sql
     *
     * @param value 参数值
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder addParam(Object value) {
        if (mode != ParameterMode.POSITIONAL) {
            throw new IllegalStateException("Can only add positional parameters in POSITIONAL mode");
        }
        if (value != null) {
            positionalParams.add(value);
        }
        return this;
    }

    /**
     * 添加OR条件表达式
     *
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
     *
     * @param column 列名
     * @param value  条件值
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder orEq(String column, Object value) {
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
     *
     * @param column    列名
     * @param paramName 参数名
     * @param value     条件值
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder orEq(String column, String paramName, Object value) {
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
            return orEq(column, value);
        }
        return this;
    }

    /**
     * 切换到命名参数模式（如果当前是位置参数模式且没有参数）
     *
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
     *
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder reset() {
        String currentSql = sql.toString();
        int whereIndex = currentSql.toUpperCase().indexOf(" WHERE ");
        if (whereIndex > 0) {
            sql.setLength(0);
            sql.append(currentSql, 0, whereIndex);
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
     *
     * @param column       列名
     * @param value        条件值
     * @param nullStrategy 空值处理策略
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder dynamicEq(String column, Object value, NullHandleStrategy nullStrategy) {
        if (!isValidColumn(column)) {
            return this;
        }

        if (nullStrategy == null) {
            nullStrategy = NullHandleStrategy.IGNORE;
        }

        // 根据策略处理null和空值
        if (value == null) {
            return switch (nullStrategy) {
                case NULL_AS_IS_NULL, NULL_AND_EMPTY_AS_IS_NULL -> isNull(column);
                case NULL_AS_IS_NOT_NULL, NULL_AND_EMPTY_AS_IS_NOT_NULL -> isNotNull(column);
                default -> this;
            };
        }

        if (value instanceof String && !StringUtils.hasText((String) value)) {
            return switch (nullStrategy) {
                case EMPTY_AS_IS_NULL, NULL_AND_EMPTY_AS_IS_NULL -> isNull(column);
                case EMPTY_AS_IS_NOT_NULL, NULL_AND_EMPTY_AS_IS_NOT_NULL -> isNotNull(column);
                default -> this;
            };
        }

        // 正常添加条件
        return eq(column, value);
    }

    /**
     * 添加LEFT LIKE条件（左匹配）
     *
     * @param column 列名
     * @param value  LEFT LIKE条件值（自动添加右侧%通配符）
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder leftLike(String column, String value) {
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
     *
     * @param column    列名
     * @param paramName 参数名
     * @param value     LEFT LIKE条件值（自动添加右侧%通配符）
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder leftLike(String column, String paramName, String value) {
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
            return leftLike(column, value);
        }

        if (inOrGroup) {
            orGroupHasConditions = true;
        }
        return this;
    }

    /**
     * 添加RIGHT LIKE条件 - 右匹配
     *
     * @param column 列名
     * @param value  RIGHT LIKE条件值（自动添加左侧%通配符）
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder rightLike(String column, String value) {
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
     *
     * @param column    列名
     * @param paramName 参数名
     * @param value     RIGHT LIKE条件值（自动添加左侧%通配符）
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder rightLike(String column, String paramName, String value) {
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
            return rightLike(column, value);
        }

        if (inOrGroup) {
            orGroupHasConditions = true;
        }
        return this;
    }

    /**
     * 添加动态LIKE条件（支持空值处理策略）
     *
     * @param column       列名
     * @param value        LIKE条件值
     * @param nullStrategy 空值处理策略
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder dynamicLike(String column, String value, NullHandleStrategy nullStrategy) {
        if (!isValidColumn(column)) {
            return this;
        }

        if (nullStrategy == null) {
            nullStrategy = NullHandleStrategy.IGNORE;
        }

        // 根据策略处理null和空值
        if (value == null) {
            return switch (nullStrategy) {
                case NULL_AS_IS_NULL, NULL_AND_EMPTY_AS_IS_NULL -> isNull(column);
                case NULL_AS_IS_NOT_NULL, NULL_AND_EMPTY_AS_IS_NOT_NULL -> isNotNull(column);
                default -> this;
            };
        }

        if (!StringUtils.hasText(value)) {
            return switch (nullStrategy) {
                case EMPTY_AS_IS_NULL, NULL_AND_EMPTY_AS_IS_NULL -> isNull(column);
                case EMPTY_AS_IS_NOT_NULL, NULL_AND_EMPTY_AS_IS_NOT_NULL -> isNotNull(column);
                default -> this;
            };
        }

        // 正常添加LIKE条件
        return like(column, value);
    }

    /**
     * 添加动态LIKE条件（默认忽略空值）
     *
     * @param column 列名
     * @param value  LIKE条件值
     * @return 当前对象（用于链式调用）
     */
    public DynamicSqlBuilder dynamicLike(String column, String value) {
        return dynamicLike(column, value, NullHandleStrategy.IGNORE);
    }


    /**
     * 获取原生SQL（将参数值直接拼接到SQL中）
     * 注意：此方法仅用于调试和日志记录，不要用于实际执行
     *
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
                sqlString = SqlUtil.replaceFirstPlaceholder(sqlString, param);
            }
        } else {
            // 处理命名参数
            MapSqlParameterSource params = getNamedParams();
            for (String paramName : params.getParameterNames()) {
                Object value = params.getValue(paramName);
                sqlString = sqlString.replace(":" + paramName,  SqlUtil.formatValue(value));
            }
        }

        return sqlString;
    }


    /**
     * 基于当前SQL构建COUNT查询语句
     * <p>自动处理ORDER BY、LIMIT、GROUP BY等子句</p>
     * <p>用于构建COUNT查询语句,用来统计总记录数</p>
     * <p>SELECT COUNT(*) FROM </p>
     *
     * @return 返回COUNT查询的SQL构建器
     */
    public DynamicSqlBuilder buildCountSql() {
        String currentSql = this.sql.toString();

        // 查找主查询的FROM位置
        int fromIndex = SqlUtil.findFromIndex(currentSql);
        if (fromIndex == -1) {
            throw new IllegalStateException("Invalid SQL statement for count query - FROM keyword not found");
        }

        // 提取FROM及其后面的部分
        String fromClause = currentSql.substring(fromIndex);

        // 移除GROUP BY子句(如果存在)
        int groupByIndex = SqlUtil.findGroupByIndex(fromClause);
        if (groupByIndex != -1) {
            fromClause = fromClause.substring(0, groupByIndex);
        }

        // 移除ORDER BY子句(如果存在)
        int orderByIndex = SqlUtil.findOrderByIndex(fromClause);
        if (orderByIndex != -1) {
            fromClause = fromClause.substring(0, orderByIndex);
        }

        // 移除LIMIT子句(如果存在)
        int limitIndex = SqlUtil.findLimitIndex(fromClause);
        if (limitIndex != -1) {
            fromClause = fromClause.substring(0, limitIndex);
        }

        // 构建新的COUNT查询，去除多余空格
        String finalCountSql = "SELECT COUNT(*)" + fromClause;

        // 创建新的构建器并复制参数(排除ORDER BY、LIMIT和GROUP BY相关的参数)
        DynamicSqlBuilder countBuilder = new DynamicSqlBuilder(finalCountSql, this.mode);

        // 计算需要排除的参数个数
        int excludeParams = SqlUtil.calculateExcludeParams(currentSql);

        // 复制参数(排除LIMIT相关参数)
        if (this.mode == ParameterMode.POSITIONAL && this.positionalParams != null) {
            int paramCount = Math.max(0, this.positionalParams.size() - excludeParams);
            for (int i = 0; i < paramCount; i++) {
                countBuilder.positionalParams.add(this.positionalParams.get(i));
            }
        } else if (this.mode == ParameterMode.NAMED && this.namedParams != null) {
            for (String paramName : this.namedParams.getParameterNames()) {
                // 排除LIMIT相关的命名参数
                if (!"limit".equals(paramName) && !"offset".equals(paramName)) {
                    countBuilder.namedParams.addValue(paramName, this.namedParams.getValue(paramName));
                }
            }
        }

        return countBuilder;
    }



}
