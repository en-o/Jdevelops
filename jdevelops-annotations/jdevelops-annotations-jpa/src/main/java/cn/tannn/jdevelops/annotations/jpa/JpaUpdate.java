package cn.tannn.jdevelops.annotations.jpa;


import java.lang.annotation.*;


/**
 * JpaUtils#updateBean() 用的注解
 *
 * <p> 如果自定义updateBean 可以参考 JpaUtils#updateBean()的方式使用本注解
 *
 * @author tan
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JpaUpdate {

    /**
     * 设置当前字段为更新用的条件（目前只支持一个）默认不设置(false)
     */
    boolean unique() default false;


    /**
     * 更新时忽略字段, 默认不忽略(false)
     */
    boolean ignore() default false;

    /**
     * 更新时自动添加时间。默认不添加(false)，[用户LocalDateTime updateTime]
     */
    boolean autoTime() default false;

}
