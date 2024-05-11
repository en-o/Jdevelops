package cn.tannn.jdevelops.annotations.jpa;


import java.lang.annotation.*;


/**
 * 内置更新用的注解，自定义更新也可用得到
 * @author tan
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JpaUpdate {

    /**
     * 更新时忽略字段, 默认不忽略
     */
    boolean ignore() default false;

    /**
     * 更新时自动添加时间。默认不添加，[用户LocalDateTime updateTime]
     */
    boolean autoTime() default false;

}
