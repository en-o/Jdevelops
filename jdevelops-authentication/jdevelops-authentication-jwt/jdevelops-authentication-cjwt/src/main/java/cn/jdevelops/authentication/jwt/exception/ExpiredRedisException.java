package cn.jdevelops.authentication.jwt.exception;

import cn.jdevelops.api.exception.exception.BusinessException;
import cn.jdevelops.api.result.emums.ExceptionCode;

/**
 * redis数据失效
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-22 12:28
 */
public class ExpiredRedisException extends BusinessException {


    public ExpiredRedisException(int code, String message) {
        super(code, message);
    }

    public ExpiredRedisException(String message) {
        super(message);
    }


    public ExpiredRedisException(ExceptionCode constantEnum) {
        super(constantEnum.getCode(), constantEnum.getMessage());
    }

    public ExpiredRedisException(ExceptionCode constantEnum, Throwable cause) {
        super(constantEnum.getMessage(), cause, constantEnum.getCode());
    }

}
