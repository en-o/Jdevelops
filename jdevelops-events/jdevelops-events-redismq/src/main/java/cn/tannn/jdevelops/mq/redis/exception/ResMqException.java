package cn.tannn.jdevelops.mq.redis.exception;


/**
 * redis mq 异常
 */
public class ResMqException extends RuntimeException {

    public ResMqException(String message, Throwable e) {
        super(message, e);
    }

    public ResMqException(String message) {
        super(message);
    }

}
