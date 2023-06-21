package cn.jdevelops.api.result.custom;

import cn.jdevelops.api.result.emums.ResultCodeEnum;
import cn.jdevelops.api.result.util.SpringBeanUtils;

/**
 * result warp.
 *
 * @author tnnn
 */
public final class ExceptionResultWrap {

    /**
     * Success object.
     *
     * @return the object
     */
    public static Object success() {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).success();
    }


    /**
     * Success object.
     *
     * @return the object
     */
    public static Object success(String message) {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).success(message);
    }

    /**
     * Success object.
     *
     * @return the object
     */
    public static Object success(Object data) {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).success(data);
    }


    /**
     * Error object.
     *
     * @return the object
     */
    public static Object error() {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).error();
    }


    /**
     * Error object.
     *
     * @return the object
     */
    public static Object error(String message) {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).error(message);
    }



    /**
     * 自定义 code 和 message
     * @param code  the code
     * @param message the message
     * @return the object
     */
    public static Object result(Integer code, String message) {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).result(code, message);
    }

    /**
     * 自定义 code 和 message
     *
     * @param resultCodeEnum the resultCodeEnum
     * @return the object
     */
    public static Object result(ResultCodeEnum resultCodeEnum) {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).result(resultCodeEnum.getCode(), resultCodeEnum.getMessage());
    }

    /**
     * SYS_ERROR
     */
    public static Object result(Exception e) {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).result(ResultCodeEnum.SYS_ERROR.getCode(), e.getMessage());
    }
}
