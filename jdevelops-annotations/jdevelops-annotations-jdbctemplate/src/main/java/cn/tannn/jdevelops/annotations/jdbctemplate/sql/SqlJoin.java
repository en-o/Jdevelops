package cn.tannn.jdevelops.annotations.jdbctemplate.sql;


import cn.tannn.jdevelops.annotations.jdbctemplate.sql.enums.JoinType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * jdbc查询 ：连接表注解[用于定义JOIN查询]
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SqlJoin {
    /**
     * 连接类型
     */
    JoinType type() default JoinType.LEFT;

    /**
     * 连接表名
     */
    String table();

    /**
     * 连接表别名
     */
    String alias();

    /**
     * 连接条件
     */
    String condition();
}
