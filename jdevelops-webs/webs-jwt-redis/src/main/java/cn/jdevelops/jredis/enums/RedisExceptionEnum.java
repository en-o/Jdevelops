package cn.jdevelops.jredis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * redis Exception 枚举
 *
 * @author tn
 * @version V1.0
 * @date 2022-07-22 12:30:44
 **/
@AllArgsConstructor
@Getter
public enum RedisExceptionEnum {

    /**
     * 登录失效，请重新登录（token不存在）
     */
    REDIS_EXPIRED_USER(403, "登录失效，请重新登录"),

    /**
     * 非法登录（token 更换过）
     */
    REDIS_NO_USER(403, "登录失效，请重新登录"),

    /**
     * 账户已禁用
     */
    DISABLED_ACCOUNT(405, "账户已禁用"),

    /**
     * 帐号被禁用帐号由于多次认证被禁用，请稍后再试
     */
    EXCESSIVE_ATTEMPTS_ACCOUNT(405, "帐号被禁用帐号由于多次认证被禁用，请稍后再试"),

    ;

    /**
     * 代码
     */
    private final int code;

    /**
     * 消息
     */
    private final String message;


}
