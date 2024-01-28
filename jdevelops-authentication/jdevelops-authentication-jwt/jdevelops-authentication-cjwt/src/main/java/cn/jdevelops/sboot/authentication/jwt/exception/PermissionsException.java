package cn.jdevelops.sboot.authentication.jwt.exception;

import cn.jdevelops.api.exception.exception.BusinessException;
import cn.jdevelops.api.result.emums.ExceptionCode;

/**
 * 接口权限
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/7/12 17:27
 */
public class PermissionsException extends BusinessException {


    public PermissionsException(int code, String message) {
        super(code, message);
    }

    public PermissionsException(String message) {
        super(message);
    }


    public PermissionsException(ExceptionCode constantEnum) {
        super(constantEnum.getCode(), constantEnum.getMessage());
    }

    public PermissionsException(ExceptionCode constantEnum, Throwable cause) {
        super(constantEnum.getMessage(), cause, constantEnum.getCode());
    }
}
