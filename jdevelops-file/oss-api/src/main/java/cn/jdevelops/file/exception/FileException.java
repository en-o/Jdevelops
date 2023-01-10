package cn.jdevelops.file.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 自定义异常
 *
 * @author tn
 * @date 2021-01-22 14:15
 */
@AllArgsConstructor
@Getter
public class FileException extends RuntimeException {

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileException(Throwable cause) {
        super(cause);
    }

    protected FileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
