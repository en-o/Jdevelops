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
     * 自定定义 code 和 message
     *
     * @param code    the code
     * @param message the message
     * @param data 数据
     * @return the t
     */
    T result(int code, String message, Object data);

    /**
     * success
     * @return the t
     */
    T success();


    /**
     * success
     * @return the t
     */
    T success(String message);

    /**
     * 自定定义 code 和 message
     *
     * @param data 数据
     * @return the t
     */
    T success(Object data);

    /**
     * Error t.
     * @return the t
     */
    T error();


    /**
     * Error t.
     * @return the t
     */
    T error(String message);

    /**
     * 自定定义 code 和 message
     *
     * @param data 数据
     * @return the t
     */
    T error(Object data);

}
