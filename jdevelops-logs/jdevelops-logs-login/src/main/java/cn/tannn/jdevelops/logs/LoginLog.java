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

    /**
     * 登录名的参数名， 用于取值（map.get(loginNameKey)）
     * <p> 如果是 String login(String loginName,String pwd) 这中方式，必须把 登录名参数放到最前面 </p>
     * @return  loginNameKey
     */
    String loginNameKey() default "loginName";
}
