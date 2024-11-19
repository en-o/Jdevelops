package cn.tannn.jdevelops.cas.exception;


/**
 * 自定义异常
 *
 * @author tn
 * @date 2021-01-22 14:15
 */
public class CasException extends RuntimeException {

    private static final long serialVersionUID = 4129812562603997310L;

    private int code;
    private String msg;


    public CasException() {
        super();
    }

    public CasException(String message) {
        super(message);
        this.msg = message;
        this.code = 401;
    }

    public CasException(String message, Throwable cause) {
        super(message, cause);
    }

    public CasException(Integer code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
