package cn.jdevelops.api.sign.enums;


/**
 * 参数异常
 *  参数错误code 20000开始
 * @author tn
 * @date 2019年07月29日 14:16
 */
public enum SginExceptionCodeEnum {


    /**
     * 消息不可读
     */
    API_SIGN_ERROR(10013,"接口签名不正确"),



    ;


    /**
     * code
     */
    private final int code;
    /**
     * 消息
     */
    private final String message;

    SginExceptionCodeEnum(int code, String message) {
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
