package cn.jdevelops.data.jap.enums;


/**
 * jpa错误code
 * 数据错误code 10000开始
 * @author tn
 * @date 2023-03-19 13:12:33
 */
public enum JpaExecptopnCodeEnum {
    /**
     * 数据访问异常
     */
    DATA_ACCESS_EXCEPTION(10001,"数据访问异常"),

    /**
     * 数据重复
     */
    DATA_REDO(10002,"数据重复"),

    /**
     * sql错误
     */
    SQL_ERROR(10003,"sql异常，详情请查看日志"),

    /**
     * 数据不存在
     */
    DATA_NOT_EXIST(10004,"数据不存在"),

    ;


    /**
     * code
     */
    private final int code;
    /**
     * 消息
     */
    private final String message;

    JpaExecptopnCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
