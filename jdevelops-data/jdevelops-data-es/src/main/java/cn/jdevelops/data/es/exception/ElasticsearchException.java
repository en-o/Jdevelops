package cn.jdevelops.data.es.exception;

/**
 * es 异常
 * @author tan
 */
public class ElasticsearchException extends RuntimeException  {
    private static final long serialVersionUID = 4129812562603997301L;

    private int code;

    public ElasticsearchException() {
    }

    public ElasticsearchException(String message) {
        super(message);
        this.code = 500;
    }

    public ElasticsearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ElasticsearchException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public ElasticsearchException(int code, String message) {
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
