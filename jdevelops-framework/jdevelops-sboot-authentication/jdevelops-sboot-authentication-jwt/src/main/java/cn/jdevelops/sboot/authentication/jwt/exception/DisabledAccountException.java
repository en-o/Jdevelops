package cn.jdevelops.sboot.authentication.jwt.exception;

import cn.jdevelops.api.exception.exception.BusinessException;
import cn.jdevelops.api.result.emums.ExceptionCode;

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
