package cn.tannn.jdevelops.webs.interceptor.exception;



/**
 *  拦截器异常
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-08-08 09:45
 */
public class InterceptorException extends RuntimeException{
    private static final long serialVersionUID = 4129812562603997310L;

    private int code;
    private String msg;


    public InterceptorException() {
        super();
    }

    public InterceptorException(String message) {
        super(message);
        this.msg = message;
        this.code = 500;
    }



    public InterceptorException(Integer code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public InterceptorException(String message, Throwable cause) {
        super(message, cause);
        this.msg = message;
        this.code = 500;
    }

    public InterceptorException(String message, Throwable cause, int code) {
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
