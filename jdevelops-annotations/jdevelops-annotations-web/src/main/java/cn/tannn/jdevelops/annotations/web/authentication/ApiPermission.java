package cn.tannn.jdevelops.annotations.web.authentication;

import java.lang.annotation.*;

/**
 * 接口权限
 * ps 参数为空则就会不严重
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2023/7/12 13:33
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ApiPermission {

    /**
     * 角色 (e.g admin)
     * ps 参数为空则就会不验证 [绝对等于]
     */
    String[] roles()  default {};

    /**
     * uri 权限  ( e.g /edit/password  接口url)
     * <p>
     *     permissions: 必须写全路径
     *     userPermissions:规则如下
     *          （2）* 匹配0个或多个字符
     *          （3）**匹配0个或多个目录
     *    参考： @see  UserRoleUtilTest
     * </p>
     * ps 参数为空则就会不验证
     */
    String permissions() default "";
}
