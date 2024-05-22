package cn.tannn.jdevelops.result.constant;


import cn.tannn.jdevelops.result.exception.ExceptionCode;

/**
 * 公共的错误code
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2023-03-19 14:13
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


    // -------------------------6 开头------------------------------------------------
    /**
     * 文件不存在
     */
    public static final ExceptionCode FILE_NO_EXIST = new ExceptionCode(601,"文件不存在");

    /**
     *  api接口调用失败
     */
    public static final ExceptionCode API_DOUBLE_CALL = new ExceptionCode(602,"短时间内请勿重复调用");



    /**
     *  动态数据源切换错误
     */
    public static final ExceptionCode DYNAMIC_DATASOURCE_SELECT = new ExceptionCode(603,"动态数据源切换错误");

}
