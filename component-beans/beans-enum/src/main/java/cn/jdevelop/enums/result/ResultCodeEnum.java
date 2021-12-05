package cn.jdevelop.enums.result;

import lombok.Getter;

/**
 * 返回值 code message 枚举
 *  result code 2开头
 * @author tn
 * @date 2019年07月29日 14:16
 */
@Getter
public enum ResultCodeEnum {

    /**
     * 成功
     */
    Success(200,"成功"),

    /**
     * 失败
     */
    ERROR(404,"页面不存在"),

    /**
     * 失败
     */
    Fail(10000,"失败"),

    /**
     * 失败
     */
    Fail5(500,"失败"),

    /**
     * 业务异常
     */
    BizError(10001,"业务异常"),

    /**
     * 入参校验异常
     */
    CheckError(10002,"入参校验异常"),

    /**
     * token 校验失败异常
     */
    TokenError(10003,"token 校验失败异常"),

    /**
     * 访问权限异常
     */
    AuthError(10004,"访问权限异常"),

    /**
     * 系统异常
     */
    SysError(10005,"系统异常"),

    /**
     * 未找到
     */
    NotFoundError(10006,"未找到"),

    /**
     * 系统限流
     */
    SysThrottling(10007,"系统限流"),

    /**
     * 系统未授权
     */
    SysUnauthorized(10008,"系统未授权"),

    /**
     * 授权过期
     */
    SysAuthorizedPast(10009,"授权过期"),

    /**
     * 数据访问异常
     */
    DataAccessException(10010,"数据访问异常"),

    /**
     * JSON格式错误
     */
    JsonError(10011,"JSON格式错误"),


    /**
     * 消息不可读
     */
    MessageNoReading(10012,"消息不可读"),
    /**
     * 数据格式检验失败
     */
    VALID_ERROR(2444,"数据格式检验失败"),
    /**
     * 用户不存在
     */
    USER_EXIST(2445,"用户不存在"),

    /**
     * token 校验失败异常
     */
    Token_Error(10003,"token检验失败");

    /**
     * code
     */
    private int code;
    /**
     * 消息
     */
    private String message;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
