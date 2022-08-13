package cn.jdevelops.result.custom;

import cn.jdevelops.enums.result.ResultCodeEnum;
import cn.jdevelops.result.util.SpringBeanUtils;

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


    /**
     * Error object.
     *
     * @param resultCodeEnum    the resultCodeEnum
     * @return the object
     */
    public static Object error(ResultCodeEnum resultCodeEnum) {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).error(resultCodeEnum.getCode(), resultCodeEnum.getMessage());
    }


    public static Object error(Exception e) {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).error(ResultCodeEnum.SYS_ERROR.getCode(), e.getMessage());
    }
}
