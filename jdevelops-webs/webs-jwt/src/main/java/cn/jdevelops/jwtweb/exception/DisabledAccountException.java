package cn.jdevelops.jwtweb.exception;

import cn.jdevelops.enums.result.TokenExceptionCodeEnum;
import cn.jdevelops.enums.result.UserExceptionEnum;
import cn.jdevelops.exception.exception.BusinessException;

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

    public DisabledAccountException(TokenExceptionCodeEnum constantEnum) {
        super(constantEnum.getCode(), constantEnum.getMessage());
    }

    public DisabledAccountException(UserExceptionEnum constantEnum) {
        super(constantEnum.getCode(), constantEnum.getMessage());
    }
}
