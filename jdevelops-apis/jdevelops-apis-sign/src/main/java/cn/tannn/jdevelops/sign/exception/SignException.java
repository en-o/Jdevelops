package cn.tannn.jdevelops.sign.exception;


import cn.tannn.jdevelops.result.exception.ExceptionCode;

/**
 * AopException
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-08-08 09:45
 */
public class SignException extends RuntimeException{
    private static final long serialVersionUID = 4129812562603997310L;

    private int code;
    private String msg;


    public SignException() {
        super();
    }

    public SignException(String message) {
        super(message);
        this.msg = message;
        this.code = 500;
    }

    public SignException(ExceptionCode resultCode) {
        super(resultCode.getMessage());
        this.msg = resultCode.getMessage();
        this.code = resultCode.getCode();
    }

    public SignException(Integer code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public SignException(String message, Throwable cause) {
        super(message, cause);
        this.msg = message;
        this.code = 500;
    }

    public SignException(String message, Throwable cause, int code) {
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
