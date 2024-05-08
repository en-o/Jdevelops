package cn.tannn.jdevelops.result.constant;


import cn.tannn.jdevelops.result.exception.ExceptionCode;

/**
 * 参数异常
 * @author tn
 * @date 2019年07月29日 14:16
 */
public class ParamCode {

    /**
     * 入参校验异常
     */
    public static final ExceptionCode CHECK_ERROR = new ExceptionCode(10011,"入参校验异常");

    /**
     * JSON格式错误
     */
    public static final ExceptionCode JSON_ERROR = new ExceptionCode(10012,"JSON格式错误");

    /**
     * 数据格式检验失败
     */
    public static final ExceptionCode VALID_ERROR = new ExceptionCode(10013,"数据格式检验失败");

    /**
     * 消息不可读
     */
    public static final ExceptionCode MESSAGE_NO_READING = new ExceptionCode(10014,"消息不可读");


    /**
     * 敏感词汇
     */
    public static final ExceptionCode SENSITIVE_WORD = new ExceptionCode(10015,"敏感词汇，请重新输入");

    /**
     * 参数不正确
     */
    public static final ExceptionCode PARAMETER_ERROR = new ExceptionCode(10016, "参数不正确");

}
