package cn.tannn.jdevelops.idempotent.annotation;


import cn.tannn.jdevelops.idempotent.config.IdempotentConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 幂等注解
 * @author 网络
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiIdempotent {

    /**
     * 自定义重复调用返回的消息
     */
    String message() default "短时间内请勿重复调用！";

    /**
     * 是否将错误code放到 HttpServletResponse 中
     * @return 默认false不放到HttpServletResponse中
     */
    boolean responseStatus() default false;


    /**
     * 幂等判断是否需要接口参数记录
     * @return 默认true 需要记录参数
     */
    boolean paramsHeader() default true;

    /**
     * 局部：过期时间 [单位 毫秒] <br/>
     * ps： -1表示使用全局的配置参数 {@link IdempotentConfig#getExpireTime()}
     */
    long expireTime() default -1;

}
