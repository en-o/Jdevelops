package cn.jdevelops.exception.exception;


/**
 * 数据冲突异常
 * @author tn
 * @date 2023-02-10 14:48:18
 */
public class DataConflictException extends BusinessException {

    public DataConflictException(String msg) {
        super(msg);
    }
}
