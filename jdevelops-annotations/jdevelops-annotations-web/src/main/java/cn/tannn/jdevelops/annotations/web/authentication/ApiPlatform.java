package cn.tannn.jdevelops.annotations.web.authentication;


import cn.tannn.jdevelops.annotations.web.constant.PlatformConstant;
import org.springframework.util.AntPathMatcher;

import java.lang.annotation.*;

/**
 * 项目接口的归属
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2023/10/31 12:18
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ApiPlatform {

    /**
     * 接口所属的前端
     * @see PlatformConstant
     */
    String[] platform() default {PlatformConstant.COMMON};


    /**
     * 注解到类的时候过滤用的 [不需要拦截的接口路径]
     * 规则判断用的 {@link AntPathMatcher}， <a href="https://www.yuque.com/tanning/mbquef/vv5ddq#paozE">所以可以使用他的规则进行填写</a>
     */
    String[] filter() default {};
}
