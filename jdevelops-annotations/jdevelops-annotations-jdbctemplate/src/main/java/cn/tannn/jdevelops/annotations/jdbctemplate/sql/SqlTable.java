package cn.tannn.jdevelops.annotations.jdbctemplate.sql;

import java.lang.annotation.*;

/**
 * jdbc查询 ： 表映射注解[用于定义实体类对应的数据库表信息]
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SqlTable {
    /**
     * 表名
     */
    String name();

    /**
     * 表别名
     */
    String alias() default "";

    /**
     * 连接表信息
     */
    SqlJoin[] joins() default {};
}
