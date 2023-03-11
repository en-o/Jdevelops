package cn.jdevelops.enums.result;


/**
 * 公共枚举
 *
 * @author tn
 * @version V1.0
 * @date 2022-07-22 12:30:44
 **/

public enum UserExceptionEnum {


    /**
     * 登录失效，请重新登录（token不存在）
     */
    REDIS_EXPIRED_USER(403, "登录失效，请重新登录"),

    /**
     * 用户不存在
     */
    USER_EXIST_ERROR(403, "账户或者密码错误，请检查后重试"),

    /**
     * 用户不存在
     */
    USER_EXIST(404,"用户不存在"),

    /**
     * 用户密码错误
     */
    USER_PASSWORD_ERROR(403, "账户或者密码错误，请检查后重试"),

    /**
     * 非法登录（token 更换过）
     */
    REDIS_NO_USER(403, "登录失效，请重新登录"),


    /**
     * 账户已封禁
     */
    BANNED_ACCOUNT(405, "账户已封禁,请联系管理员"),

    /**
     * 帐号被禁用帐号由于多次认证被禁用，请稍后再试
     */
    EXCESSIVE_ATTEMPTS_ACCOUNT(405, "帐号被禁用帐号由于多次认证被禁用,请稍后再试"),

    /**
     * 非白名单用户
     */
    WHITE_LIST_ACCOUNT(405, "非白名单用户,请联系管理员"),


    /**
     *  账号待审核,请联系管理员
     */
    AUDIT_ACCOUNT(405, " 账号待审核,请联系管理员"),


    /**
     * 密码不一致，请重新输入
     */
    PWD_VERIFY(406,"密码不一致，请重新输入"),

    /**
     * 用户正在同步中，请稍后再试或者联系管理员
     */
    USER_SYNC_LOAING(500, "用户正在同步中，请稍后再试或者联系管理员"),


    /**
     * 账户已停用
     */
    DISABLED_ACCOUNT(407, "账户已停用"),


    ;

    /**
     * 代码
     */
    private final int code;

    /**
     * 消息
     */
    private final String message;


    UserExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
