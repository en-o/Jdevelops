package cn.tannn.jdevelops.renewpwd.exception;


import java.util.Objects;

/**
 *
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
