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
    public static String SYMBOL  = "<=====>";
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
        this.code = IDEMPOTENT_CODE;
    }


    /**
     * 特的message
     *   <ps>用于无法使用BusinessException，当时想自定义失败code的情况（默认是501）</ps>
     * @param message message
     * @return code+ ExceptionResultWrap.symbol+message; (eg. 200<=====>你错了)
     */
    public static IdempotentException specialMessage(String message){
        return new IdempotentException(IDEMPOTENT_CODE + SYMBOL + message);
    }


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
