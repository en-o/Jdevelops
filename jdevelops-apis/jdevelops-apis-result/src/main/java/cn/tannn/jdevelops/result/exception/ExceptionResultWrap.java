package cn.tannn.jdevelops.result.exception;

import cn.tannn.jdevelops.result.constant.ResultCode;
import cn.tannn.jdevelops.result.spring.SpringContextUtils;
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
        return SpringContextUtils.getInstance().getBean(ExceptionResult.class).success();
    }

    /**
     * Success object.
     *
     * @return the object
     */
    public static Object success(Object body) {
        return SpringContextUtils.getInstance().getBean(ExceptionResult.class).success(body);
    }

    /**
     * Error object.
     *
     * @return the object
     */
    public static Object error() {
        return SpringContextUtils.getInstance().getBean(ExceptionResult.class).fail();
    }


    /**
     * Error object.
     *
     * @return the object
     */
    public static Object error(String message) {
        return SpringContextUtils.getInstance().getBean(ExceptionResult.class).fail(message);
    }


    /**
     * 自定义 code 和 message
     * @param code  the code
     * @param message the message
     * @return the object
     */
    public static Object result(Integer code, String message) {
        return SpringContextUtils.getInstance().getBean(ExceptionResult.class).result(code, message);
    }

    /**
     * 自定义 code 和 message
     *
     * @param resultCode the resultCodeEnum
     * @return the object
     */
    public static Object result(ExceptionCode resultCode) {
        return SpringContextUtils.getInstance().getBean(ExceptionResult.class).result(resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * SYS_ERROR
     */
    public static Object result(Exception e) {

        try {
            // @see https://www.yuque.com/tanning/yg9ipo/lvsmwtdzfi3i8egs#qQSv6
            String message = e.getMessage();
            if (message != null  && message.contains(SYMBOL)) {
                String[] split = message.split(SYMBOL);
                int code = Integer.parseInt(split[0]);
                return SpringContextUtils.getInstance().getBean(ExceptionResult.class).result(code, split[1]);
            }
        }catch (Exception e2){
            LOG.warn("解析"+SYMBOL+"失败",e);
        }
        return SpringContextUtils.getInstance().getBean(ExceptionResult.class).result(ResultCode.SYS_ERROR.getCode(), e.getMessage());
    }


}
