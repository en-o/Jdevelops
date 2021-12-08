package cn.jdevelops.jap.annotation;

import cn.jdevelops.jap.core.util.criteria.ExpandCriterion;

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
     * sql 表达式连接词
     * <pre>
     * 作用于 CommUtils.getSelectBean
     *  根据注解内容进行条件拼接，例如： 用的EQ 则： where 字段 = 值
     * </pre>
     *
     */
    ExpandCriterion.Operator operator() default ExpandCriterion.Operator.EQ;

    /**
     *  true 空值不做查询，false 不忽略空
     */
    boolean ignoreNull() default true;
}
