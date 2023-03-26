package cn.jdevelops.data.jap.annotation;


import cn.jdevelops.data.jap.enums.SQLOperatorWrapper;

import java.lang.annotation.*;

/**
 * jpa查询条件
 *
 * @author tn
 * @version 1
 * @date 2021-12-08 15:21
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JpaSelectWrapperOperator {

    /**
     * sql 运算符 (Specifications方法用)
     * <pre>
     *  根据注解内容进行条件拼接，例如： 用的EQ 则： where 字段 = 值
     * </pre>
     *
     */
    SQLOperatorWrapper operatorWrapper() default SQLOperatorWrapper.EQ;

    /**
     *  true 空值不做查询，false 不忽略空
     */
    boolean ignoreNull() default true;

    /**
     * 自定义查询用的字段名
     * 空时默认使用属性字段
     */
    String fieldName() default "";

}
