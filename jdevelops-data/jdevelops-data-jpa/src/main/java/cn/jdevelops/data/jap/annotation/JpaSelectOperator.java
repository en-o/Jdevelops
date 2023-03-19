package cn.jdevelops.data.jap.annotation;


import cn.jdevelops.data.jap.enums.SQLConnect;
import cn.jdevelops.data.jap.enums.SQLOperator;

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
public @interface JpaSelectOperator {

    /**
     * sql 运算符
     * <pre>
     * 作用于 CommUtils.getSelectBean
     *  根据注解内容进行条件拼接，例如： 用的EQ 则： where 字段 = 值
     * </pre>
     *
     */
    SQLOperator operator() default SQLOperator.EQ;

    /**
     *  true 空值不做查询，false 不忽略空
     */
    boolean ignoreNull() default true;

    /**
     * sql 连接符
     * <pre>
     * 作用于 CommUtils.getSelectBean
     *  根据注解内容进行条件拼接，例如： 用的AND 则： where 字段 = 值 and 字段 = 值
     * </pre>
     *
     */
    SQLConnect connect() default SQLConnect.AND;
}
