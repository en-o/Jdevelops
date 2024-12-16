package cn.tannn.jdevelops.jdectemplate.exception;


/**
 * 自定义异常
 *
 * @author tn
 * @date 2021-01-22 14:15
 */
public class JdbcTemplateException extends RuntimeException {

    private static final long serialVersionUID = 4129812562603997310L;

    private int code;
    private String msg;


    public JdbcTemplateException() {
        super();
    }

    public JdbcTemplateException(String message) {
        super(message);
        this.msg = message;
        this.code = 401;
    }

    public JdbcTemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public JdbcTemplateException(Integer code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public JdbcTemplateException(Throwable cause, Integer code, String message) {
        super(message, cause);
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
