package cn.jdevelops.sboot.authentication.jwt.annotation;

import java.lang.annotation.*;

/**
 * 接口权限
 * ps 参数为空则就会不严重
 *
 * @author <a href="https://tannn.cn/">tan</a>
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
     * 权限  (e.g read)
     * ps 参数为空则就会不验证 [绝对等于，目前还没有做url前缀验证]
     */
    String[] permissions()  default {};
}
