package cn.tannn.jdevelops.logs;

import cn.tannn.jdevelops.logs.constant.LoginType;

import java.lang.annotation.*;

/**
 * 登录日志记录（包括登录与退出）
 *
 * @author tn
 * @date 2025-04-09 16:09:39
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginLog {

    /**
     * true 登录 | false 退出
     */
    boolean login() default true;

    /**
     * 登录类型
     * @see LoginType
     */
   String type();

    /**
     * 表达式，可以用#{参数名} or #{bean.name} 取入参值
     */
    String expression() default "";
}
