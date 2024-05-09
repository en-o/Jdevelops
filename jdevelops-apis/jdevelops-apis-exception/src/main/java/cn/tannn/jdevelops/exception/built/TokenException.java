package cn.tannn.jdevelops.exception.built;


import cn.tannn.jdevelops.result.exception.ExceptionCode;

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

    public TokenException(ExceptionCode constantEnum) {
        super(constantEnum.getCode(), constantEnum.getMessage());
    }

}
