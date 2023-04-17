package cn.jdevelops.sboot.authentication.jwt.exception;

import cn.jdevelops.api.exception.exception.BusinessException;
import cn.jdevelops.api.result.emums.TokenExceptionCodeEnum;
import cn.jdevelops.api.result.emums.UserExceptionEnum;

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
