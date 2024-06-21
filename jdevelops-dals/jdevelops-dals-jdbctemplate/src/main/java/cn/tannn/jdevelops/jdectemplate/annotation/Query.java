package cn.tannn.jdevelops.jdectemplate.annotation;


import java.lang.annotation.*;

/**
 * 查询注解，必须使用在继承类的方法上才能生效
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
