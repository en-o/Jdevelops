package cn.tannn.jdevelops.annotations.jpa;


import java.lang.annotation.*;
import java.time.LocalDateTime;


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
     * <p> 如果是 {@link LocalDateTime} 类型可以使用  {@link JpaUpdate#autoTime()}  注解来自动复制
     * <p> 如何不是 那就自己手动在 getter 方法里处理下把（应为我没测过
     */
    boolean autoTime() default false;

}
