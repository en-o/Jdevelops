package cn.jdevelops.util.authorization.error.exception;

import cn.jdevelops.api.result.emums.ExceptionCode;
import cn.jdevelops.api.result.emums.ResultCode;

import static cn.jdevelops.api.result.custom.ExceptionResultWrap.SYMBOL;

/**
 * 异常
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/1/26 9:39
 */
public class AuthorizationException extends RuntimeException{

    private int code;

    public AuthorizationException() {
    }


    public AuthorizationException(String message) {
        super(message);
        this.code = ResultCode.FAIL.getCode();
    }


    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public AuthorizationException(int code, String message) {
        super(message);
        this.code = code;
    }

    public AuthorizationException(ExceptionCode code, String message) {
        super(message);
        this.code = code.getCode();
    }

    public AuthorizationException(ExceptionCode exceptionCode){
        super(exceptionCode.getMessage());
        this.code = exceptionCode.getCode();
    }

    /**
     * 特的message
     *   <ps>用于无法使用BusinessException，当时想自定义失败code的情况（默认是501）</ps>
     * @param message message
     * @return code+ ExceptionResultWrap.symbol+message; (eg. 200<=====>你错了)
     */
    public static AuthorizationException specialMessage(String message){
        return new AuthorizationException(ResultCode.FAIL.getCode() + SYMBOL + message);
    }

    public int getCode() {
        return this.code;
    }

    public String getErrorMessage() {
        return super.getMessage();
    }
}
