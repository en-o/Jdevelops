package cn.tannn.jdevelops.renewpwd.exception;


/**
 * 密码续命检测异常
 * <p>用于在密码续命检测过程中抛出异常
 * @author tan
 */
public final class StopDetectorException extends RuntimeException {

    private static final long serialVersionUID = 5224696788505678598L;

    public StopDetectorException() {
    }

    public StopDetectorException(String message) {
        super(message);
    }

    public StopDetectorException(String message, Throwable cause) {
        super(message, cause);
    }

    public StopDetectorException(Throwable cause) {
        super(cause);
    }


}
