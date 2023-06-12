package cn.jdevelops.data.jap.annotation;


import cn.jdevelops.data.jap.enums.SQLConnect;
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
     *  true表示会判断value是否为空，空则不做查询条件，不空则做查询条件
     */
    boolean ignoreNull() default true;

    /**
     * 自定义查询用的字段名
     * 空时默认使用属性字段
     */
    String fieldName() default "";



    /**
     * sql 连接符
     * <pre>
     *  根据注解内容进行条件拼接，例如： 用的AND 则： where 字段 = 值 and 字段 = 值
     * </pre>
     *
     */
    SQLConnect connect() default SQLConnect.AND;

}
