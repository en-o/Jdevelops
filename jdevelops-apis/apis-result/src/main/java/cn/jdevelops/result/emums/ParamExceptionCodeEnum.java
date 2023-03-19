package cn.jdevelops.result.emums;


/**
 * 参数异常
 *  参数错误code 20000开始
 * @author tn
 * @date 2019年07月29日 14:16
 */
public enum ParamExceptionCodeEnum {
    /**
     * JSON格式错误
     */
    JSON_ERROR(10011,"JSON格式错误"),

    /**
     * 消息不可读
     */
    MESSAGE_NO_READING(10012,"消息不可读"),

    /**
     * 消息不可读
     */
    API_SIGN_ERROR(10013,"接口签名不正确"),

    /**
     * 敏感词汇
     */
    SENSITIVE_WORD(10014,"敏感词汇，请重新输入"),

    /**
     * 参数不正确
     */
    PARAMETER_ERROR(10015,"参数不正确"),

    /**
     * 入参校验异常
     */
    CHECK_ERROR(10002,"入参校验异常"),

    /**
     * 数据格式检验失败
     */
    VALID_ERROR(2444,"数据格式检验失败"),

    ;


    /**
     * code
     */
    private final int code;
    /**
     * 消息
     */
    private final String message;

    ParamExceptionCodeEnum(int code, String message) {
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
