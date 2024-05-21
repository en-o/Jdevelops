package cn.tannn.jdevelops.exception.built;


import cn.tannn.jdevelops.result.exception.ExceptionCode;

/**
 * AopException
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-08-08 09:45
 */
public class AopException extends BusinessException{
    
    public AopException(int code, String message) {
        super(code, message);
    }
    public AopException(String msg) {
        super(msg);
    }
    public AopException(String message, Throwable cause) {
        super(message, cause);
    }
    public AopException(ExceptionCode constantEnum) {
        super(constantEnum.getCode(), constantEnum.getMessage());
    }
}
