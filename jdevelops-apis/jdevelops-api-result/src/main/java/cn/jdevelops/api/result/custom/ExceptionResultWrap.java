package cn.jdevelops.api.result.custom;

import cn.jdevelops.api.result.emums.ExceptionCode;
import cn.jdevelops.api.result.emums.ResultCode;
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
    public static Object success(Object body) {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).success(body);
    }

    /**
     * Error object.
     *
     * @return the object
     */
    public static Object error() {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).fail();
    }


    /**
     * Error object.
     *
     * @return the object
     */
    public static Object error(String message) {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).fail(message);
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
     * @param resultCode the resultCodeEnum
     * @return the object
     */
    public static Object result(ExceptionCode resultCode) {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).result(resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * SYS_ERROR
     */
    public static Object result(Exception e) {
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).result(ResultCode.SYS_ERROR.getCode(), e.getMessage());
    }


}
