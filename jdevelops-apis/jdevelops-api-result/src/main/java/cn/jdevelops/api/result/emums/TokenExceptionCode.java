package cn.jdevelops.api.result.emums;


/**
 * token 异常 message
 *
 * @author tn
 * @date 2019年07月29日 14:16
 */
public class TokenExceptionCode {



    /**
     * 访问权限异常
     */
    public static final ExceptionCode AUTH_ERROR = new ExceptionCode(10004, "访问权限异常");


    /**
     * token 校验失败异常
     */
    public static final ExceptionCode TOKEN_ERROR = new ExceptionCode(403, "登录失效，请重新登录");

    /**
     * redis 登录失效，请重新登录（token不存在）
     */
    public static final ExceptionCode REDIS_EXPIRED_USER = new ExceptionCode(403, "登录失效，请重新登录");

    /**
     * 非法登录（token 更换过）
     */
    public static final ExceptionCode REDIS_NO_USER = new ExceptionCode(403, "登录失效，请重新登录");


    /**
     * 系统未授权
     */
    public static final ExceptionCode UNAUTHENTICATED = new ExceptionCode(401, "系统未授权,请先登录");

    /**
     * 授权过期
     */
    public static final ExceptionCode SYS_AUTHORIZED_PAST = new ExceptionCode(402, "授权过期");

    /**
     * 单点登录失败
     */
    public static final ExceptionCode CAS_LOGIN_ERROR = new ExceptionCode(2447, "单点登录失败");


}
