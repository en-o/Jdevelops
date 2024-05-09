package cn.tannn.jdevelops.annotations.web.authentication;


import cn.tannn.jdevelops.annotations.web.constant.PlatformConstant;

import java.lang.annotation.*;

/**
 * 项目接口的归属
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/10/31 12:18
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ApiPlatform {

    /**
     * 接口所属的前端
     */
    PlatformConstant[] platform() default {PlatformConstant.COMMON};
}
