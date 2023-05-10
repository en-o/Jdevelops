package cn.jdevelops.aop.api.log.annotation;


import cn.jdevelops.aop.api.log.enums.OperateTypeEnum;

import java.lang.annotation.*;

/**
 *
 * 自定义注解类 记录接口的操作日志
 *  注解放置的目标位置,METHOD是可注解在方法级别上
 *  注解在哪个阶段执行
 * @author tn
 * @date  2020/6/1 20:53
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiLog {

    /**
     * 操作分类
     * 实际并不是数组，因为枚举不能设置 null 作为默认值
     */
    OperateTypeEnum type();

    /**
     * 日志存储需要用的的一些东西，自己设置自己解析
     * @return String
     */
    String description() default "";


    /**
     * 表达式，可以用#{参数名} or #{bean.name} 取入参值
     */
    String expression() default "";

    /**
     * 接口名中文名
     * @return String
     */
    String chineseApi() default "";


    // ========== 开关字段 ==========

    /**
     * 是否记录操作日志 ,false 不记录
     */
    boolean enable() default true;
    /**
     * 是否记录方法参数 ,false 不记录
     */
    boolean logArgs() default true;
    /**
     * 是否记录方法结果的数据 ,false 不记录
     */
    boolean logResultData() default true;

}
