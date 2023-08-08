package cn.jdevelops.api.result.custom;

import cn.jdevelops.api.result.emums.ExceptionCode;
import cn.jdevelops.api.result.emums.ResultCode;
import cn.jdevelops.api.result.util.SpringBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * result warp.
 *
 * @author tnnn
 */
public final class ExceptionResultWrap {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionResultWrap.class);
    public static String SYMBOL  = "<=====>";

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

        try {
            String message = e.getMessage();
            if (message != null  && message.contains(SYMBOL)) {
                String[] split = message.split(SYMBOL);
                int code = Integer.parseInt(split[0]);
                return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).result(code, split[1]);
            }
        }catch (Exception e2){
            LOG.warn("解析"+SYMBOL+"失败",e);
        }
        return SpringBeanUtils.getInstance().getBean(ExceptionResult.class).result(ResultCode.SYS_ERROR.getCode(), e.getMessage());
    }


}
