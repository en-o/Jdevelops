package cn.jdevelops.sboot.authentication.jwt.annotation;

import java.lang.annotation.*;

/**
 * 接口权限
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
     */
    String[] roles()  default {};

    /**
     * 权限  (e.g read)
     */
    String[] permissions()  default {};
}
