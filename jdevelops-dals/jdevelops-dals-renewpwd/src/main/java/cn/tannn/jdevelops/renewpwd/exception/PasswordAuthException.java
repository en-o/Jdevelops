package cn.tannn.jdevelops.renewpwd.exception;


/**
 *  密码验证错误
 * @author tan
 */
public final class PasswordAuthException extends RuntimeException {
    private static String defMsg =  "数据库密码配置错误，主密码和备用密码都无法连接数据库，请检查配置文件或环境变量";
    private static final long serialVersionUID = 5224696788505678598L;

    /**
     * 默认构造函数，使用默认错误消息
     */
    public PasswordAuthException() {
        super(defMsg);
    }

    public PasswordAuthException(String message) {
        super(message);
    }


    public PasswordAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordAuthException(Throwable cause) {
        super(cause);
    }


}
