package cn.jdevelops.result.emums;


/**
 * 公共的错误code
 * @author tn
 * @date 2019年07月29日 14:16
 */
public enum ResultCodeEnum {
    /**
     * 成功
     */
    SUCCESS(200,"成功"),

    /**
     * 失败
     */
    ERROR(404,"页面不存在"),

    /**
     * 服务异常
     */
    FAIL(500,"服务异常"),

    /**
     * 系统异常
     */
    SYS_ERROR(501,"系统异常"),

    /**
     * 系统限流
     */
    SYS_THROTTLING(503,"系统限流"),




    ;


    /**
     * code
     */
    private final int code;
    /**
     * 消息
     */
    private final String message;

    ResultCodeEnum(int code, String message) {
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
