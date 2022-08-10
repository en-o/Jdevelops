package cn.jdevelops.exception;


/**
 * AopException
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-08-08 09:45
 */
public class AopException extends RuntimeException{
    private static final long serialVersionUID = 4129812562603997310L;

    private int code;
    private String msg;


    public AopException() {
        super();
    }

    public AopException(String message) {
        super(message);
        this.msg = message;
        this.code = 500;
    }

    public AopException(Integer code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
