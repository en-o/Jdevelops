package cn.jdevelops.api.result.emums;

/**
 * 异常代码
 *
 * @author tan
 */
public class ExceptionCode {

    /**
     * code
     */
    private final int code;

    /**
     * 消息
     */
    private final String message;

    public ExceptionCode(int code, String message) {
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
