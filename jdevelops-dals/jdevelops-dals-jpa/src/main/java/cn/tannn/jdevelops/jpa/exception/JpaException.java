package cn.tannn.jdevelops.jpa.exception;

import cn.tannn.jdevelops.annotations.jpa.enums.JpaExecptopnCodeEnum;

/**
 * jpa异常
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-08-08 09:45
 */
public class JpaException extends RuntimeException{
    private static final long serialVersionUID = 4129812562603997310L;

    private int code;
    private String msg;


    public JpaException() {
        super();
    }

    public JpaException(String message) {
        super(message);
        this.msg = message;
        this.code = JpaExecptopnCodeEnum.DATA_ACCESS_EXCEPTION.getCode();
    }

    public JpaException(String message, Throwable cause) {
        super(message, cause);
    }

    public JpaException(Integer code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public JpaException(JpaExecptopnCodeEnum resultCode2Enum){
        super(resultCode2Enum.getMessage());
        this.code = resultCode2Enum.getCode();
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
