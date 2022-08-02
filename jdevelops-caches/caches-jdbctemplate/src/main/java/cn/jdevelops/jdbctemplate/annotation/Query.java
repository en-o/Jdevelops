package cn.jdevelops.jdbctemplate.annotation;


import java.lang.annotation.*;

/**
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
     *
     * @return 返回的实体对象（String.class, Bean.class
     */
    Class clazz() ;
}
