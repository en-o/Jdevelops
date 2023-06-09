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
     * sql 运算符 (JpaUtils方法用)
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
     * 自定义查询用的字段名
     * 空时默认使用属性字段
     */
    String fieldName() default "";


    /**
     * 字段在sql中的类型，用于时间函数查询时（vo写的string 但数据库是时间类型查询出错的问题，格式只能为ymdmss） 一般时间就写长城 Date.class就行了
     */
    Class<?> sqlType() default String.class;


    /**
     * sql 连接符 (JpaUtils方法用)
     * <pre>
     * 作用于 CommUtils.getSelectBean
     *  根据注解内容进行条件拼接，例如： 用的AND 则： where 字段 = 值 and 字段 = 值
     * </pre>
     *
     */
    SQLConnect connect() default SQLConnect.AND;
}
