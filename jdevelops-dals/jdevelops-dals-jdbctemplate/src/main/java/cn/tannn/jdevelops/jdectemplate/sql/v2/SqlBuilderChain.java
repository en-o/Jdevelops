package cn.tannn.jdevelops.jdectemplate.sql.v2;

import cn.tannn.jdevelops.jdectemplate.annotations.*;
import cn.tannn.jdevelops.jdectemplate.sql.DynamicSqlBuilder;
import cn.tannn.jdevelops.jdectemplate.sql.ParameterMode;
import org.springframework.util.StringUtils;

import java.util.function.Consumer;

/**
 * SQL构建链式调用器 - 提供有序的SQL构建流程
 * 构建顺序：SELECT → FROM/JOIN → WHERE → GROUP BY → HAVING → ORDER BY → LIMIT/OFFSET
 */
public class SqlBuilderChain {

    private final Object queryObj;
    private final ParameterMode mode;
    private DynamicSqlBuilder builder;

    // 状态标记，确保调用顺序
    private boolean selectBuilt = false;
    private boolean fromBuilt = false;
    private boolean whereBuilt = false;
    private boolean groupByBuilt = false;
    private boolean havingBuilt = false;
    private boolean orderByBuilt = false;
    private boolean limitBuilt = false;

    private SqlBuilderChain(Object queryObj, ParameterMode mode) {
        this.queryObj = queryObj;
        this.mode = mode;
        if (queryObj == null) {
            throw new IllegalArgumentException("Query object cannot be null");
        }
    }

    /**
     * 创建链式构建器
     */
    public static SqlBuilderChain create(Object queryObj) {
        return new SqlBuilderChain(queryObj, ParameterMode.NAMED);
    }

    /**
     * 创建链式构建器（指定参数模式）
     */
    public static SqlBuilderChain create(Object queryObj, ParameterMode mode) {
        return new SqlBuilderChain(queryObj, mode);
    }

    // ================================ SELECT 阶段 ================================

    /**
     * 构建SELECT部分 - 基于返回类型自动构建
     */
    public SqlBuilderChain select(Class<?> returnClazz) {
        ensureNotBuilt(selectBuilt, "SELECT");
        String selectSql = SqlFieldBuilder.buildSelectFields(returnClazz);
        this.builder = new DynamicSqlBuilder("SELECT " + selectSql, mode);
        this.selectBuilt = true;
        return this;
    }

    /**
     * 构建SELECT部分 - 使用自定义SELECT
     */
    public SqlBuilderChain select(String customSelect) {
        ensureNotBuilt(selectBuilt, "SELECT");
        this.builder = new DynamicSqlBuilder("SELECT " + customSelect, mode);
        this.selectBuilt = true;
        return this;
    }

    /**
     * 构建SELECT部分 - 扩展方式
     */
    public SqlBuilderChain select(Class<?> returnClazz, Consumer<StringBuilder> selectExtender) {
        ensureNotBuilt(selectBuilt, "SELECT");
        StringBuilder selectBuilder = new StringBuilder("SELECT ");
        if (returnClazz != null) {
            selectBuilder.append(SqlFieldBuilder.buildSelectFields(returnClazz));
        } else {
            selectBuilder.append("*");
        }
        if (selectExtender != null) {
            selectExtender.accept(selectBuilder);
        }
        this.builder = new DynamicSqlBuilder(selectBuilder.toString(), mode);
        this.selectBuilt = true;
        return this;
    }

    // ================================ FROM/JOIN 阶段 ================================

    /**
     * 构建FROM部分 - 基于注解自动构建
     */
    public SqlBuilderChain from() {
        ensureBuilt(selectBuilt, "SELECT");
        ensureNotBuilt(fromBuilt, "FROM");

        Class<?> clazz = queryObj.getClass();
        String fromSql = SqlTableBuilder.buildFromClause(clazz);
        builder.sql.append(" ").append(fromSql);
        this.fromBuilt = true;
        return this;
    }

    /**
     * 构建FROM部分 - 使用完整的FROM...JOIN语句
     */
    public SqlBuilderChain from(String completeFromClause) {
        ensureBuilt(selectBuilt, "SELECT");
        ensureNotBuilt(fromBuilt, "FROM");

        builder.sql.append(" ").append(completeFromClause);
        this.fromBuilt = true;
        return this;
    }

    /**
     * 构建FROM部分 - 扩展方式
     */
    public SqlBuilderChain from(Consumer<StringBuilder> fromExtender) {
        ensureBuilt(selectBuilt, "SELECT");
        ensureNotBuilt(fromBuilt, "FROM");

        Class<?> clazz = queryObj.getClass();
        StringBuilder fromBuilder = new StringBuilder(SqlTableBuilder.buildFromClause(clazz));
        if (fromExtender != null) {
            fromExtender.accept(fromBuilder);
        }
        builder.sql.append(" ").append(fromBuilder);
        this.fromBuilt = true;
        return this;
    }

    // ================================ WHERE 阶段 ================================

    /**
     * 构建WHERE部分 - 基于注解自动构建
     */
    public SqlBuilderChain where() {
        ensureBuilt(fromBuilt, "FROM");
        ensureNotBuilt(whereBuilt, "WHERE");

        SqlConditionProcessor.processQueryConditions(builder, queryObj);
        this.whereBuilt = true;
        return this;
    }

    /**
     * 构建WHERE部分 - 扩展方式
     */
    public SqlBuilderChain where(Consumer<DynamicSqlBuilder> whereExtender) {
        ensureBuilt(fromBuilt, "FROM");
        ensureNotBuilt(whereBuilt, "WHERE");

        SqlConditionProcessor.processQueryConditions(builder, queryObj);
        if (whereExtender != null) {
            whereExtender.accept(builder);
        }
        this.whereBuilt = true;
        return this;
    }

    /**
     * 构建WHERE部分 - 仅扩展，不使用注解
     */
    public SqlBuilderChain whereCustom(Consumer<DynamicSqlBuilder> whereExtender) {
        ensureBuilt(fromBuilt, "FROM");
        ensureNotBuilt(whereBuilt, "WHERE");

        if (whereExtender != null) {
            whereExtender.accept(builder);
        }
        this.whereBuilt = true;
        return this;
    }

    // ================================ GROUP BY 阶段 ================================

    /**
     * 构建GROUP BY部分
     */
    public SqlBuilderChain groupBy(String groupByClause) {
        ensureBuilt(whereBuilt, "WHERE");
        ensureNotBuilt(groupByBuilt, "GROUP BY");

        if (StringUtils.hasText(groupByClause)) {
            builder.addGroupBy(groupByClause);
        }
        this.groupByBuilt = true;
        return this;
    }

    /**
     * 构建GROUP BY部分 - 扩展方式
     */
    public SqlBuilderChain groupBy(Consumer<StringBuilder> groupByExtender) {
        ensureBuilt(whereBuilt, "WHERE");
        ensureNotBuilt(groupByBuilt, "GROUP BY");

        StringBuilder groupByBuilder = new StringBuilder();
        if (groupByExtender != null) {
            groupByExtender.accept(groupByBuilder);
            if (!groupByBuilder.isEmpty()) {
                builder.addGroupBy(groupByBuilder.toString());
            }
        }
        this.groupByBuilt = true;
        return this;
    }

    // ================================ HAVING 阶段 ================================

    /**
     * 构建HAVING部分
     */
    public SqlBuilderChain having(String havingClause) {
        ensureBuilt(groupByBuilt, "GROUP BY");
        ensureNotBuilt(havingBuilt, "HAVING");

        if (StringUtils.hasText(havingClause)) {
            builder.having(havingClause);
        }
        this.havingBuilt = true;
        return this;
    }

    /**
     * 构建HAVING部分 - 扩展方式
     */
    public SqlBuilderChain having(Consumer<StringBuilder> havingExtender) {
        ensureBuilt(groupByBuilt, "GROUP BY");
        ensureNotBuilt(havingBuilt, "HAVING");

        StringBuilder havingBuilder = new StringBuilder();
        if (havingExtender != null) {
            havingExtender.accept(havingBuilder);
            if (!havingBuilder.isEmpty()) {
                builder.having(havingBuilder.toString());
            }
        }
        this.havingBuilt = true;
        return this;
    }

    // ================================ ORDER BY 阶段 ================================

    /**
     * 构建ORDER BY部分 - 基于注解自动构建
     */
    public SqlBuilderChain orderBy() {
//        ensureBuilt(havingBuilt, "HAVING");
        ensureBuilt(groupByBuilt || !havingBuilt, "GROUP BY or skip HAVING");
        ensureNotBuilt(orderByBuilt, "ORDER BY");

        SqlOrderProcessor.processOrderBy(builder, queryObj.getClass());
        this.orderByBuilt = true;
        return this;
    }

    /**
     * 构建ORDER BY部分 - 自定义排序
     */
    public SqlBuilderChain orderBy(String orderByClause) {
//        ensureBuilt(havingBuilt, "HAVING");
        ensureBuilt(groupByBuilt || !havingBuilt, "GROUP BY or skip HAVING");
        ensureNotBuilt(orderByBuilt, "ORDER BY");

        // 先处理注解
        SqlOrderProcessor.processOrderBy(builder, queryObj.getClass());

        if (StringUtils.hasText(orderByClause)) {
            builder.orderBy(orderByClause);
        }
        this.orderByBuilt = true;
        return this;
    }

    /**
     * 构建ORDER BY部分 - 扩展方式
     */
    public SqlBuilderChain orderBy(Consumer<StringBuilder> orderByExtender) {
//        ensureBuilt(havingBuilt, "HAVING");
        ensureBuilt(groupByBuilt || !havingBuilt, "GROUP BY or skip HAVING");
        ensureNotBuilt(orderByBuilt, "ORDER BY");

        // 首先处理注解
        SqlOrderProcessor.processOrderBy(builder, queryObj.getClass());

        // 然后扩展
        if (orderByExtender != null) {
            StringBuilder orderByBuilder = new StringBuilder();
            orderByExtender.accept(orderByBuilder);
            if (!orderByBuilder.isEmpty()) {
                builder.orderBy(orderByBuilder.toString());
            }
        }
        this.orderByBuilt = true;
        return this;
    }

    // ================================ LIMIT/OFFSET 阶段 ================================

    /**
     * 构建分页部分 - 基于注解自动构建
     */
    public SqlBuilderChain limit() {
        ensureBuilt(orderByBuilt, "ORDER BY");
        ensureNotBuilt(limitBuilt, "LIMIT");

        SqlPageProcessor.processPage(builder, queryObj);
        this.limitBuilt = true;
        return this;
    }

    /**
     * 构建分页部分 - 自定义分页
     */
    public SqlBuilderChain limit(Integer pageIndex, Integer pageSize) {
        ensureBuilt(orderByBuilt, "ORDER BY");
        ensureNotBuilt(limitBuilt, "LIMIT");

        // 先处理注解
        SqlPageProcessor.processPage(builder, queryObj);

        if (pageIndex != null && pageSize != null) {
            builder.pageZero(pageIndex, pageSize);
        }
        this.limitBuilt = true;
        return this;
    }

    /**
     * 构建分页部分 - 扩展方式
     */
    public SqlBuilderChain limit(Consumer<DynamicSqlBuilder> limitExtender) {
        ensureBuilt(orderByBuilt, "ORDER BY");
        ensureNotBuilt(limitBuilt, "LIMIT");

        // 首先处理注解
        SqlPageProcessor.processPage(builder, queryObj);

        // 然后扩展
        if (limitExtender != null) {
            limitExtender.accept(builder);
        }
        this.limitBuilt = true;
        return this;
    }

    // ================================ 构建完成 ================================

    /**
     * 完成构建，返回DynamicSqlBuilder
     */
    public DynamicSqlBuilder build() {
        // 确保所有阶段都已完成（至少要完成到WHERE）
        if (!selectBuilt) {
            throw new IllegalStateException("SELECT clause must be built before calling build()");
        }
        if (!fromBuilt) {
            throw new IllegalStateException("FROM clause must be built before calling build()");
        }

        // 如果后续阶段未构建，使用默认值
        if (!whereBuilt) {
            where();
        }
        if (!groupByBuilt) {
            groupBy((String) null);
        }
        if (!havingBuilt) {
            having((String) null);
        }
        if (!orderByBuilt) {
            orderBy();
        }
        if (!limitBuilt) {
            limit();
        }

        return builder;
    }

    // ================================ 工具方法 ================================

    private void ensureBuilt(boolean built, String stage) {
        if (!built) {
            throw new IllegalStateException(stage + " must be built before this operation");
        }
    }

    private void ensureNotBuilt(boolean built, String stage) {
        if (built) {
            throw new IllegalStateException(stage + " has already been built");
        }
    }

    /**
     * 快捷构建方法 - 基本查询（保持向后兼容）
     */
    public static DynamicSqlBuilder buildBasic(Object queryObj, Class<?> returnClazz) {
        return create(queryObj)
                .select(returnClazz)
                .from()
                .where()
                .groupBy((String) null)
                .having((String) null)
                .orderBy()
                .limit()
                .build();
    }

    /**
     * 快捷构建方法 - 自定义SELECT（保持向后兼容）
     */
    public static DynamicSqlBuilder buildWithCustomSelect(Object queryObj, String customSelect) {
        return create(queryObj)
                .select(customSelect)
                .from()
                .where()
                .groupBy((String) null)
                .having((String) null)
                .orderBy()
                .limit()
                .build();
    }

    /**
     * 获取底层构建器的StringBuilder，用于高级操作
     */
    public StringBuilder getRawSql() {
        if (builder == null) {
            throw new IllegalStateException("Builder not initialized. Call select() first.");
        }
        return builder.sql;
    }

    /**
     * 获取当前状态信息（用于调试）
     */
    public String getStatus() {
        return String.format("Status: SELECT=%s, FROM=%s, WHERE=%s, GROUP BY=%s, HAVING=%s, ORDER BY=%s, LIMIT=%s",
                selectBuilt, fromBuilt, whereBuilt, groupByBuilt, havingBuilt, orderByBuilt, limitBuilt);
    }
}
