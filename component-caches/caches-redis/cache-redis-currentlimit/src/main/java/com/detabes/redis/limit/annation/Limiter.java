package com.detabes.redis.limit.annation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author lmz
 * @projectName currentlimit-redis
 * @packageName com.lmz.code.currentlimit.annation
 * @company Peter
 * @date 2021/8/19  14:43
 * @description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Limiter {
    long DEFAULT_REQUEST = 10;

    /**
     * max 最大请求数
     */
    @AliasFor("max") long value() default DEFAULT_REQUEST;

    /**
     * max 最大请求数
     */
    @AliasFor("value") long max() default DEFAULT_REQUEST;

    /**
     * 限流key
     */
    String key() default "";

    /**
     * 限流时长，默认30
     */
    long timeout() default 30;

    /**
     * 限流时长单位，默认 秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
