package cn.jdevelops.api.exception.exception;

import cn.jdevelops.api.result.emums.UserExceptionEnum;

/**
 * redis数据失效
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-22 12:28
 */
public class UserException extends BusinessException {


    public UserException(int code, String message) {
        super(code, message);
    }

    public UserException(String message) {
        super(message);
    }
    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(UserExceptionEnum constantEnum) {
        super(constantEnum.getCode(), constantEnum.getMessage());
    }
}
