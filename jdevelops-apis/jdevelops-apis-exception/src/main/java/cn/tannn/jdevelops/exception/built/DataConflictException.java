package cn.tannn.jdevelops.exception.built;


import cn.tannn.jdevelops.result.exception.ExceptionCode;

/**
 * 数据冲突异常
 * @author tn
 * @date 2023-02-10 14:48:18
 */
public class DataConflictException extends BusinessException {

    public DataConflictException(int code, String message) {
        super(code, message);
    }
    public DataConflictException(String msg) {
        super(msg);
    }
    public DataConflictException(String message, Throwable cause) {
        super(message, cause);
    }
    public DataConflictException(ExceptionCode constantEnum) {
        super(constantEnum.getCode(), constantEnum.getMessage());
    }
}
