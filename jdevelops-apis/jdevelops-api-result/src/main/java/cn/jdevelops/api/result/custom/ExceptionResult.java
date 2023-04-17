package cn.jdevelops.api.result.custom;

import cn.jdevelops.api.result.emums.ResultCodeEnum;

/**
 * 全局异常返回类
 * @param <T>
 * @author tnnn
 */
public interface ExceptionResult<T> {


    /**
     * 自定定义 code 和 message
     * @param resultCodeEnum the message
     * @return the t
     */
    T result(ResultCodeEnum resultCodeEnum);

    /**
     * 自定定义 code 和 message
     *
     * @param code    the code
     * @param message the message
     * @return the t
     */
    T result(int code, String message);


    /**
     * success
     * @return the t
     */
    T success();


    /**
     * Error t.
     * @return the t
     */
    T error();

}
