package cn.jdevelops.exception.result;

import cn.jdevelops.exception.utils.SpringBeanUtils;

/**
 *  result warp.
 * @author tnnn
 */
public final class ExceptionResultWrap {
    
    /**
     * Success object.
     *
     * @param code    the code
     * @param message the message
     * @param object  the object
     * @return the object
     */
    public static Object success(final int code, final String message, final Object object) {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).success(code, message, object);
    }

    /**
     * Error object.
     *
     * @param code    the code
     * @param message the message
     * @param object  the object
     * @return the object
     */
    public static Object error(final int code, final String message, final Object object) {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).error(code, message, object);
    }


    /**
     * Error object.
     *
     * @param code    the code
     * @param message the message
     * @return the object
     */
    public static Object error(final int code, final String message) {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).error(code, message);
    }
}