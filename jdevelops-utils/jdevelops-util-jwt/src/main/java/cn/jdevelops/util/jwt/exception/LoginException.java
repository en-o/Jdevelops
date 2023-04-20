package cn.jdevelops.util.jwt.exception;

import cn.jdevelops.api.exception.exception.BusinessException;
import cn.jdevelops.api.result.emums.TokenExceptionCodeEnum;
import cn.jdevelops.api.result.emums.UserExceptionEnum;

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


    public LoginException(TokenExceptionCodeEnum constantEnum) {
        super(constantEnum.getCode(), constantEnum.getMessage());
    }

    public LoginException(TokenExceptionCodeEnum constantEnum, Throwable cause) {
        super(constantEnum.getMessage(), cause, constantEnum.getCode());
    }

    public LoginException(UserExceptionEnum constantEnum) {
        super(constantEnum.getCode(), constantEnum.getMessage());
    }

    public LoginException(UserExceptionEnum constantEnum, Throwable cause) {
        super(constantEnum.getMessage(), cause, constantEnum.getCode());
    }
}
