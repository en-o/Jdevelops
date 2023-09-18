package cn.jdevelops.data.ddss.exception;


/**
 * 动态数据库相关异常
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/9/15 13:49
 */
public class DynamicDataSourceException extends RuntimeException {
    private static final long serialVersionUID = 4129812562603997311L;

    /**
     * ResultCode.DYNAMIC_DATASOURCE_SELECT
     */
    public static int IDEMPOTENT_CODE = 603;
    public static String SYMBOL = "<=====>";


    private int code;

    public DynamicDataSourceException() {
    }

    public DynamicDataSourceException(String message) {
        super(message);
        this.code = IDEMPOTENT_CODE;
    }

    public DynamicDataSourceException(String message, Throwable cause) {
        super(message, cause);
        this.code = IDEMPOTENT_CODE;
    }

    /**
     * 特的message
     * <ps>用于无法使用BusinessException，当时想自定义失败code的情况（默认是501）</ps>
     *
     * @param message message
     * @return code+ ExceptionResultWrap.symbol+message; (eg. 200<=====>你错了)
     */
    public static DynamicDataSourceException specialMessage(String message) {
        return new DynamicDataSourceException(IDEMPOTENT_CODE + SYMBOL + message);
    }

    /**
     * 特的message
     * <ps>用于无法使用BusinessException，当时想自定义失败code的情况（默认是501）</ps>
     *
     * @param message message
     * @return code+ ExceptionResultWrap.symbol+message; (eg. 200<=====>你错了)
     */
    public static DynamicDataSourceException specialMessage(String message, Throwable cause) {
        return new DynamicDataSourceException(IDEMPOTENT_CODE + SYMBOL + message, cause);
    }

    /**
     * 特的message
     * <ps>用于无法使用BusinessException，当时想自定义失败code的情况（默认是501）</ps>
     *
     * @param message message
     * @return code+ ExceptionResultWrap.symbol+message; (eg. 200<=====>你错了)
     */
    public static DynamicDataSourceException specialMessage(int code, String message, Throwable cause) {
        return new DynamicDataSourceException(IDEMPOTENT_CODE + SYMBOL + message, cause, code);
    }


    public DynamicDataSourceException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public DynamicDataSourceException(int code, String message) {
        super(message);
        this.code = code;
    }


    public int getCode() {
        return this.code;
    }

    public String getErrorMessage() {
        return super.getMessage();
    }


}
