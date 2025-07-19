package cn.tannn.jdevelops.jdectemplate.annotations;

import cn.tannn.jdevelops.jdectemplate.enums.QueryType;
import cn.tannn.jdevelops.jdectemplate.sql.NullHandleStrategy;

import java.lang.annotation.*;

/**
 * jdbc查询 ：字段映射注解[用于定义实体字段与数据库字段的映射关系]
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SqlColumn {

    /**
     * 数据库字段名
     */
    String name() default "";

    /**
     * 表别名（如果字段来自连接表）
     */
    String tableAlias() default "";

    /**
     * 查询类型
     */
    QueryType queryType() default QueryType.EQ;

    /**
     * 空值处理策略
     */
    NullHandleStrategy nullStrategy() default NullHandleStrategy.IGNORE;

    /**
     * 是否参与查询条件
     */
    boolean queryable() default true;

    /**
     * 自定义操作符（当 queryType 为 CUSTOM 时使用）
     */
    String operator() default "";

    /**
     * 参数名（命名参数模式下使用）
     */
    String paramName() default "";
}
