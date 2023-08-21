package cn.jdevelops.api.result.emums;


/**
 * 权限与异常
 *
 * @author tn
 * @date 2019年07月29日 14:16
 */
public class PermissionsExceptionCode {


    /**
     * 访问权限异常
     */
    public static final ExceptionCode AUTH_ERROR = new ExceptionCode(10004, "访问权限异常");


    /**
     * 接口无访问权限 - 无角色
     */
    public static final ExceptionCode API_ROLE_AUTH_ERROR = new ExceptionCode(10005, "接口无访问权限");


    /**
     * 接口无访问权限 - 权限
     */
    public static final ExceptionCode API_PERMISSION_AUTH_ERROR = new ExceptionCode(10005, "接口无访问权限");

}
