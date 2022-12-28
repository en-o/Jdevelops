package cn.jdevelops.jredis.exception;

import cn.jdevelops.enums.result.TokenExceptionCodeEnum;
import cn.jdevelops.enums.result.UserExceptionEnum;
import cn.jdevelops.exception.exception.BusinessException;

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

    public ExpiredRedisException(TokenExceptionCodeEnum constantEnum) {
        super(constantEnum.getCode(), constantEnum.getMessage());
    }

    public ExpiredRedisException(UserExceptionEnum constantEnum) {
        super(constantEnum.getCode(), constantEnum.getMessage());
    }
}
