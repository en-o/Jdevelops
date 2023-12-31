package cn.jdevelops.webs.websocket.exception;

/**
 * WebSocket异常
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-12-31 15:30
 */
public class WebSocketException extends RuntimeException {

    public WebSocketException() {
        super();
    }

    public WebSocketException(String message) {
        super(message);
    }

    public WebSocketException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebSocketException(Throwable cause) {
        super(cause);
    }

    protected WebSocketException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
