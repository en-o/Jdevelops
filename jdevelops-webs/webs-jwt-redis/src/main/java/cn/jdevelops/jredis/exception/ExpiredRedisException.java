package cn.jdevelops.jredis.exception;

import cn.jdevelops.exception.exception.BusinessException;
import cn.jdevelops.jredis.enums.RedisExceptionEnum;

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

    public ExpiredRedisException(RedisExceptionEnum constantEnum) {
        super(constantEnum.getCode(), constantEnum.getMessage());
    }
}
