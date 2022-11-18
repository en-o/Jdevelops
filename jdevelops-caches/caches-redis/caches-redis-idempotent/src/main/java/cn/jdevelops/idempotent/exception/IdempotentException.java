package cn.jdevelops.idempotent.exception;

/**
 * 接口幂等异常
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-11-17 15:02
 */
public class IdempotentException  extends RuntimeException{
    private static final long serialVersionUID = 4129812562603997310L;

    private int code;
    private String msg;

    public IdempotentException() {
        super();
    }

    public IdempotentException(String message) {
        super(message);
        this.msg = message;
        this.code = 500;
    }

    public IdempotentException(Integer code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public IdempotentException(String message, Throwable cause) {
        super(message, cause);
        this.msg = message;
        this.code = 500;
    }

    public IdempotentException(String message, Throwable cause, int code) {
        super(message, cause);
        this.msg = message;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
