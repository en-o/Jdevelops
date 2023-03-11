package cn.jdevelops.enums.result;


/**
 *  token 异常 message
 *  result code 2开头
 * @author tn
 * @date 2019年07月29日 14:16
 */
public enum TokenExceptionCodeEnum {

    /**
     * token 校验失败异常
     */
    TOKEN_ERROR(10003,"登录失效，请重新登录"),

    /**
     * 访问权限异常
     */
    AUTH_ERROR(10004,"访问权限异常"),


    /**
     * 系统未授权
     */
    @Deprecated
    SYS_UNAUTHORIZED(10008,"系统未授权,请先登录"),


    /**
     * 系统未授权
     */
    UNAUTHENTICATED(401, "系统未授权,请先登录"),

    /**
     * 授权过期
     */
    SYS_AUTHORIZED_PAST(10009,"授权过期"),

    /**
     * 单点登录失败
     */
    CAS_LOGIN_ERROR(2447,"单点登录失败"),



    ;


    /**
     * code
     */
    private final int code;
    /**
     * 消息
     */
    private final String message;

    TokenExceptionCodeEnum(int code, String message) {
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
