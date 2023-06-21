package cn.jdevelops.api.result.exception;



import cn.jdevelops.api.result.emums.ExceptionCode;

import java.util.Objects;

/**
 * 业务逻辑异常 Exception
 * @author tan
 */
public final class ServiceException extends RuntimeException {

    /**
     * 业务错误码
     */
    private Integer code;
    /**
     * 错误提示
     */
    private String message;

    /**
     * 空构造方法，避免反序列化问题
     */
    public ServiceException() {
    }

    public ServiceException(ExceptionCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public ServiceException(ExceptionCode errorCode, Throwable cause) {
        super(errorCode.getMessage(),cause);
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public ServiceException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceException)) {
            return false;
        }
        ServiceException that = (ServiceException) o;
        return Objects.equals(code, that.code) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
