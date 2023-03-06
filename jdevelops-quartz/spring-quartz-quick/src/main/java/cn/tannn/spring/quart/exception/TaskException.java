package cn.tannn.spring.quart.exception;


/**
 *  任务错误
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-22 12:28
 */
public class TaskException extends RuntimeException {

    private static final long serialVersionUID = 4129812562603997310L;

    private int code;
    private String msg;


    public TaskException() {
        super();
    }

    public TaskException(String message) {
        super(message);
        this.msg = message;
        this.code = 500;
    }

    public TaskException(Integer code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public TaskException(String message, Throwable cause) {
        super(message, cause);
        this.msg = message;
        this.code = 500;
    }

    public TaskException(String message, Throwable cause, int code) {
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
