package cn.jdevelops.api.result.emums;

import cn.jdevelops.api.result.custom.ExceptionResultWrap;

/**
 * 异常代码
 *
 * @author tan
 */
public class ExceptionCode {

    /**
     * code
     */
    private final int code;

    /**
     * 消息
     */
    private final String message;

    public ExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 特的message
     *   <ps>用于无法使用BusinessException，当时想自定义失败code的情况（默认是501）</ps>
     * @param code code
     * @param message message
     * @return code+ ExceptionResultWrap.symbol+message; (eg. 200<=====>你错了)
     */
    public static String specialMessage(int code, String message){
        return code + ExceptionResultWrap.SYMBOL + message;
    }

}
