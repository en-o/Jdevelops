package cn.jdevelops.jap.annotation;


import java.lang.annotation.*;

/**
 * 使用 JpaUtils.getSelectBean（2） 时忽略一些不需要的字段
 *
 * @author tn
 * @version 1
 * @date 2021-12-08 15:21
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JpaSelectIgnoreField {

}
