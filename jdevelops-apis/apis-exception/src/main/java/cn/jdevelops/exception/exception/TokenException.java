package cn.jdevelops.exception.exception;


import cn.jdevelops.enums.result.TokenExceptionCodeEnum;

/**
 * jwt异常
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-12-28 09:33
 */
public class TokenException extends BusinessException{
    public TokenException(int code, String message) {
        super(code, message);
    }

    public TokenException(String message) {
        super(message);
    }
    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenException(TokenExceptionCodeEnum constantEnum) {
        super(constantEnum.getCode(), constantEnum.getMessage());
    }

}
