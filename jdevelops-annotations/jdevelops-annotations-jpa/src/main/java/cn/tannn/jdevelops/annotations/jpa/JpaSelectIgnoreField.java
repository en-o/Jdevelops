package cn.tannn.jdevelops.annotations.jpa;


import java.lang.annotation.*;

/**
 *  查询忽略一些不需要的字段 （配合 EnhanceSpecification 使用）
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
