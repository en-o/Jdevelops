package cn.jdevelops.util.funasr.exception;

/**
 * Funasr 异常
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-12-31 15:30
 */
public class FunasrException extends RuntimeException {

    public FunasrException() {
        super();
    }

    public FunasrException(String message) {
        super(message);
    }

    public FunasrException(String message, Throwable cause) {
        super(message, cause);
    }

    public FunasrException(Throwable cause) {
        super(cause);
    }

    protected FunasrException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
