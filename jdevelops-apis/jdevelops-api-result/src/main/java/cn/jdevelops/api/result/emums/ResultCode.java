package cn.jdevelops.api.result.emums;


/**
 * 公共的错误code
 *
 * @author tn
 * @date 2019年07月29日 14:16
 */
public class ResultCode {

    /**
     * 成功
     */
    public static final ExceptionCode SUCCESS = new ExceptionCode(200, "成功");

    /**
     * 失败
     */
    public static final ExceptionCode FAIL = new ExceptionCode(500, "失败");

    /**
     * 404
     */
    public static final ExceptionCode ERROR = new ExceptionCode(404, "页面不存在");

    /**
     * 系统异常
     */
    public static final ExceptionCode SYS_ERROR = new ExceptionCode(501, "系统异常");

    /**
     * 系统限流
     */
    public static final ExceptionCode SYS_THROTTLING = new ExceptionCode(503, "系统限流");


    /**
     * 数据重复
     */
    public static final ExceptionCode DATA_REPEAT = new ExceptionCode(503, "数据重复");

    /**
     * 数据不存在
     */
    public static final ExceptionCode DATA_NONEXISTENCE = new ExceptionCode(504, "数据不存在");



}
