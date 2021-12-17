package cn.jdevelops.exception.exception;

import cn.jdevelops.enums.result.ResultCodeEnum;

/**
 * 自定义异常
 * @author tn
 * @version 1
 * @date 2020/6/17 15:07
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 4129812562603997310L;

    private int code;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
        this.code = ResultCodeEnum.FAIL.getCode();
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultCodeEnum code, String message) {
        super(message);
        this.code = code.getCode();
    }

    public BusinessException(ResultCodeEnum resultCode2Enum){
        super(resultCode2Enum.getMessage());
        this.code = resultCode2Enum.getCode();
    }


    public int getCode() {
        return this.code;
    }

    public String getErrorMessage() {
        return super.getMessage();
    }
}
