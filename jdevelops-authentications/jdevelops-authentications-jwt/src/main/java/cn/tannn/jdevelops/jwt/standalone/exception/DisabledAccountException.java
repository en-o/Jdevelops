package cn.tannn.jdevelops.jwt.standalone.exception;

import cn.tannn.jdevelops.exception.built.BusinessException;
import cn.tannn.jdevelops.result.exception.ExceptionCode;

/**
 * 用户异常
 * @author tnnn
 */
public class DisabledAccountException extends BusinessException {

    public DisabledAccountException(int code, String message) {
        super(code, message);
    }

    public DisabledAccountException(String message) {
        super(message);
    }

    public DisabledAccountException(ExceptionCode constantEnum, Throwable cause) {
        super(constantEnum.getMessage(), cause, constantEnum.getCode());
    }

    public DisabledAccountException(ExceptionCode constantEnum) {
        super(constantEnum.getCode(), constantEnum.getMessage());
    }
}
