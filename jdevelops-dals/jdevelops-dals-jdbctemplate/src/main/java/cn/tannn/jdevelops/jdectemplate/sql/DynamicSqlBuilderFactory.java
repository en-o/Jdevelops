package cn.tannn.jdevelops.jdectemplate.sql;

import cn.tannn.jdevelops.annotations.jdbctemplate.sql.SqlJoin;
import cn.tannn.jdevelops.annotations.jdbctemplate.sql.SqlTable;
import cn.tannn.jdevelops.annotations.jdbctemplate.sql.enums.ParameterMode;
import cn.tannn.jdevelops.jdectemplate.sql.v2.SqlBuilderChain;
import org.springframework.util.StringUtils;

import java.util.function.Consumer;

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
     * 创建链式构建器 - 推荐使用
     */
    public static SqlBuilderChain chain(Object queryObj) {
        return SqlBuilderChain.create(queryObj);
    }

    /**
     * 创建链式构建器 - 指定参数模式
     */
    public static SqlBuilderChain chain(Object queryObj, ParameterMode mode) {
        return SqlBuilderChain.create(queryObj, mode);
    }

    /**
     * 构建基本查询 - 最简单的用法
     */
    public static DynamicSqlBuilder basic(Object queryObj, Class<?> returnClazz) {
        return SqlBuilderChain.buildBasic(queryObj, returnClazz);
    }

    /**
     * 构建自定义SELECT查询
     */
    public static DynamicSqlBuilder customSelect(Object queryObj, String selectClause) {
        return SqlBuilderChain.buildWithCustomSelect(queryObj, selectClause);
    }

    /**
     * 构建复杂查询 - 提供完整的FROM子句
     */
    public static DynamicSqlBuilder complexQuery(Object queryObj,
                                                 String selectClause,
                                                 String fromClause) {
        return SqlBuilderChain.create(queryObj)
                .select(selectClause)
                .from(fromClause)
                .where()
                .groupBy((String) null)
                .having((String) null)
                .orderBy()
                .limit()
                .build();
    }

    /**
     * 构建统计查询 - 常用于COUNT、SUM等统计
     */
    public static DynamicSqlBuilder statQuery(Object queryObj,
                                              String selectClause,
                                              String groupByClause) {
        return SqlBuilderChain.create(queryObj)
                .select(selectClause)
                .from()
                .where()
                .groupBy(groupByClause)
                .having((String) null)
                .orderBy()
                .limit()
                .build();
    }

    /**
     * 高级构建方法 - 提供所有阶段的扩展能力
     */
    public static class AdvancedBuilder {
        private final Object queryObj;
        private final ParameterMode mode;

        private AdvancedBuilder(Object queryObj, ParameterMode mode) {
            this.queryObj = queryObj;
            this.mode = mode;
        }

        public static AdvancedBuilder with(Object queryObj) {
            return new AdvancedBuilder(queryObj, ParameterMode.NAMED);
        }

        public static AdvancedBuilder with(Object queryObj, ParameterMode mode) {
            return new AdvancedBuilder(queryObj, mode);
        }

        /**
         * 完全自定义的构建方式
         */
        public DynamicSqlBuilder build(Consumer<SqlBuilderChain> chainBuilder) {
            SqlBuilderChain chain = SqlBuilderChain.create(queryObj, mode);
            if (chainBuilder != null) {
                chainBuilder.accept(chain);
            }
            return chain.build();
        }

        /**
         * 分步构建 - 每个阶段都可以扩展
         */
        public DynamicSqlBuilder buildWithExtensions(
                Consumer<StringBuilder> selectExtender,
                Consumer<StringBuilder> fromExtender,
                Consumer<DynamicSqlBuilder> whereExtender,
                Consumer<StringBuilder> groupByExtender,
                Consumer<StringBuilder> havingExtender,
                Consumer<StringBuilder> orderByExtender,
                Consumer<DynamicSqlBuilder> limitExtender) {

            SqlBuilderChain chain = SqlBuilderChain.create(queryObj, mode);

            // SELECT阶段
            if (selectExtender != null) {
                chain.select(Object.class, selectExtender);
            } else {
                chain.select(Object.class);
            }

            // FROM阶段
            if (fromExtender != null) {
                chain.from(fromExtender);
            } else {
                chain.from();
            }

            // WHERE阶段
            if (whereExtender != null) {
                chain.where(whereExtender);
            } else {
                chain.where();
            }

            // GROUP BY阶段
            if (groupByExtender != null) {
                chain.groupBy(groupByExtender);
            } else {
                chain.groupBy((String) null);
            }

            // HAVING阶段
            if (havingExtender != null) {
                chain.having(havingExtender);
            } else {
                chain.having((String) null);
            }

            // ORDER BY阶段
            if (orderByExtender != null) {
                chain.orderBy(orderByExtender);
            } else {
                chain.orderBy();
            }

            // LIMIT阶段
            if (limitExtender != null) {
                chain.limit(limitExtender);
            } else {
                chain.limit();
            }

            return chain.build();
        }
    }

    /**
     * 获取高级构建器
     */
    public static AdvancedBuilder advanced(Object queryObj) {
        return AdvancedBuilder.with(queryObj);
    }

    /**
     * 获取高级构建器 - 指定参数模式
     */
    public static AdvancedBuilder advanced(Object queryObj, ParameterMode mode) {
        return AdvancedBuilder.with(queryObj, mode);
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


    // ================================ 示例用法注释 ================================

    /*
     * 基本用法示例：
     *
     * 1. 最简单的查询：
     *    DynamicSqlBuilder builder = DynamicSqlBuilderFactory.basic(queryObj, UserVO.class);
     *
     * 2. 自定义SELECT：
     *    DynamicSqlBuilder builder = DynamicSqlBuilderFactory.customSelect(queryObj, "id, name, age");
     *
     * 3. 链式构建：
     *    DynamicSqlBuilder builder = DynamicSqlBuilderFactory.chain(queryObj)
     *        .select(UserVO.class)
     *        .from()
     *        .where(builder -> builder.eq("status", 1))
     *        .orderBy("create_time DESC")
     *        .limit(0, 10)
     *        .build();
     *
     * 4. 复杂查询：
     *    DynamicSqlBuilder builder = DynamicSqlBuilderFactory.complexQuery(queryObj,
     *        "u.id, u.name, d.dept_name",
     *        "FROM users u LEFT JOIN departments d ON u.dept_id = d.id");
     *
     * 5. 统计查询：
     *    DynamicSqlBuilder builder = DynamicSqlBuilderFactory.statQuery(queryObj,
     *        "dept_id, COUNT(*) as user_count",
     *        "dept_id");
     *
     * 6. 高级自定义：
     *    DynamicSqlBuilder builder = DynamicSqlBuilderFactory.advanced(queryObj)
     *        .build(chain -> chain
     *            .select("DISTINCT u.id, u.name")
     *            .from(sb -> sb.append(" LEFT JOIN user_roles ur ON u.id = ur.user_id"))
     *            .where(builder -> builder.eq("ur.role_id", roleId))
     *            .orderBy("u.name ASC")
     *            .limit(pageIndex, pageSize));
     */
}
