package cn.jdevelops.api.idempotent.exception;

/**
 * 接口幂等异常
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-11-17 15:02
 */
public class IdempotentException  extends RuntimeException{
    /**
     * ResultCode.API_DOUBLE_CALL
     */
    public static int IDEMPOTENT_CODE = 602;
    private static final long serialVersionUID = 4129812562603997310L;

    private int code;
    private String msg;

    public IdempotentException() {
        super();
    }

    public IdempotentException(String message) {
        super(message);
        this.msg = message;

        this.code = IDEMPOTENT_CODE;
    }


    public IdempotentException(String message, Throwable cause) {
        super(message, cause);
        this.msg = message;
        // ResultCode.API_DOUBLE_CALL
        this.code = IDEMPOTENT_CODE;
    }



    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
