package cn.jdevelops.api.idempotent.annotation;

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
}
