package cn.tannn.jdevelops.result.exception;

/**
 * 全局返回类接口
 *
 * @param <T>
 * @author tnnn
 */
public interface ExceptionResult<T> {

    /**
     * 自定定义 code 和 message
     *
     * @param resultCode the message
     * @return the t
     */
    T result(ExceptionCode resultCode);

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
     * @param data    数据
     * @return the t
     */
    T result(int code, String message, Object data);


    /**
     * ResultCode
     *
     * @param resultCode 状态
     * @param data       数据
     * @return the t
     */
    T result(ExceptionCode resultCode, Object data);


    /**
     * success
     *
     * @return T
     */
    T success();

    /**
     * success
     *
     * @param data data
     * @return T
     */
    T success(Object data);


    /**
     * fail
     *
     * @param message message
     * @return T
     */
    T fail(String message);

    /**
     * fail
     *
     * @return T
     */
    T fail();

}
