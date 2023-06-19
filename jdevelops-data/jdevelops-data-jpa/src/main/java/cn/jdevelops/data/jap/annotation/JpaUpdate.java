package cn.jdevelops.data.jap.annotation;


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

}
