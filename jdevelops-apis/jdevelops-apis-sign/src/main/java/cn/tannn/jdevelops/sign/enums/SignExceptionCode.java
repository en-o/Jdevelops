package cn.tannn.jdevelops.sign.enums;


import cn.tannn.jdevelops.result.exception.ExceptionCode;

/**
 * 参数异常
 *  参数错误code 20000开始
 * @author tn
 * @date 2019年07月29日 14:16
 */
public class SignExceptionCode {

    /**
     * 接口签名不正确
     */
    public static final ExceptionCode API_SIGN_ERROR  = new ExceptionCode(10013, "接口签名不正确");



}
