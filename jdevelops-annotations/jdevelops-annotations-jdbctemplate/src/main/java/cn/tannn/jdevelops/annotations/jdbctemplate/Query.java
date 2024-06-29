package cn.tannn.jdevelops.annotations.jdbctemplate;


import java.lang.annotation.*;

/**
 * 查询注解
 * @author tnnn
 * @date 2022-08-01 11:50:342
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Documented
public @interface Query {
    /**
     * @return sql 语句
     */
    String value() ;
}
