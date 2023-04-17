package cn.jdevelops.sboot.web.exception;

/**
 * @author tnnn
 */
public class JHttpException extends RuntimeException {
    private static final long serialVersionUID = 5224696788505678598L;

    public JHttpException() {
    }

    public JHttpException(String message) {
        super(message);
    }

    public JHttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public JHttpException(Throwable cause) {
        super(cause);
    }
}
