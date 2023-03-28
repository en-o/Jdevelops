package cn.jdevelops.aop.api.log.annotation;


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
     * 日志存储需要用的的一些东西，自己设置自己解析
     * @return String
     */
    String description() default "";

    /**
     * 接口名中文名
     * @return String
     */
    String chineseApi() default "";

}
