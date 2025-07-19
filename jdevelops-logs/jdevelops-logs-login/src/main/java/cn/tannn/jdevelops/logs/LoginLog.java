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
     * <p> 如果是 String login(String loginName,String pwd) 不支持这种 </p>
     * <p> 只支持 String login(Bean login)  </p>
     * @return  loginNameKey
     */
    String loginNameKey() default "loginName";

    /**
     * 登录平台【逗号隔开，一般不会有多个】
     * <p> 登录参数中的 platform 值为准，如果是分开写的接口没有platform参数则使用这个</p>
     * <p> 参数名写死 </p>
     * @see cn.tannn.jdevelops.annotations.web.constant.PlatformConstant
     */
    String platform() default "WEB_ADMIN";
}
