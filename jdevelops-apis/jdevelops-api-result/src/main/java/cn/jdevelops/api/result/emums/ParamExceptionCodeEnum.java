package cn.jdevelops.api.result.emums;


/**
 * 参数异常
 * @author tn
 * @date 2019年07月29日 14:16
 */
public enum ParamExceptionCodeEnum {

    /**
     * 入参校验异常
     */
    CHECK_ERROR(10011,"入参校验异常"),
    /**
     * JSON格式错误
     */
    JSON_ERROR(10011,"JSON格式错误"),

    /**
     * 数据格式检验失败
     */
    VALID_ERROR(10011,"数据格式检验失败"),

    /**
     * 消息不可读
     */
    MESSAGE_NO_READING(10012,"消息不可读"),

    /**
     * 敏感词汇
     */
    SENSITIVE_WORD(10014,"敏感词汇，请重新输入"),

    /**
     * 参数不正确
     */
    PARAMETER_ERROR(10015,"参数不正确"),


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
