package cn.tannn.jdevelops.redis.limit;

import org.springframework.data.spel.spi.Function;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登录次数限制
 * @author tan
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginLimit {

    /**
     * 自定义重复调用返回的消息
     */
    String message() default "频繁登录请稍后再试！";

    /**
     * 是否修改 http请求的status[默认false都是200, true=403],不是返回的json的status
     * @return 默认false不放到HttpServletResponse中
     */
    boolean responseStatus() default true;

    /**
     * 局部：过期时间 [单位 毫秒] <br/>
     * ps： -1表示使用全局的配置参数 {@link LoginLimitConfig#getExpireTime()}
     */
    long expireTime() default -1;

    /**
     * 密码错误次数
     * <pr>
     * 默认5
     * </pr>
     */
    int limit() default 5;

    /**
     * SpEL表达式，用于获取参数值
     * 例如：#username 或 #user.username
     */
    String spel() default "";
}
