package cn.jdevelops.apilog.annotation;


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
     * 调用方 key
     * @return String
     */
    String apiKey() default "";

}