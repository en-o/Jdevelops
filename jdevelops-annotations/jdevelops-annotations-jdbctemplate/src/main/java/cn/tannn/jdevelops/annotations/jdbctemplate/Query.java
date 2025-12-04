package cn.tannn.jdevelops.annotations.jdbctemplate;


import java.lang.annotation.*;

/**
 * 查询注解 配合 {@link JdbcTemplate} 进行在接口方法上直接写sql用的
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


    /**
     * 吞掉异常 - [只是查询异常] 目前跟内部的EmptyResultDataAccessException处理是一样的，后续考虑这个功能扩大比如返回一个空对象
     * @return 默认false
     */
    boolean tryc() default false;
}
