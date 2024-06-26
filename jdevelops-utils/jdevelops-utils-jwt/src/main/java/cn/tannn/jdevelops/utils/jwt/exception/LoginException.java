package cn.tannn.jdevelops.utils.jwt.exception;


import cn.tannn.jdevelops.exception.built.BusinessException;
import cn.tannn.jdevelops.result.exception.ExceptionCode;

/**
 * JwtException
 *
 * @author tnnn
 */
public class LoginException extends BusinessException {
    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginException(int code, String message, Throwable cause) {
        super(message, cause, code);
    }


    public LoginException(ExceptionCode constantEnum) {
        super(constantEnum.getCode(), constantEnum.getMessage());
    }

    public LoginException(ExceptionCode constantEnum, Throwable cause) {
        super(constantEnum.getCode(), constantEnum.getMessage());
    }

}
