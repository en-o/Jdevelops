package cn.jdevelops.result.custom;

/**
 * 全局异常返回类
 * @param <T>
 * @author tnnn
 */
public interface ExceptionResult<T> {

    /**
     * Success t.
     *
     * @param code    the code
     * @param message the message
     * @param object  the object
     * @return the t
     */
    T success(int code, String message, Object object);

    /**
     * Error t.
     *
     * @param code    the code
     * @param message the message
     * @param object  the object
     * @return the t
     */
    T error(int code, String message, Object object);
    /**
     * Error t.
     *
     * @param code    the code
     * @param message the message
     * @return the t
     */
    T error(int code, String message);
}
