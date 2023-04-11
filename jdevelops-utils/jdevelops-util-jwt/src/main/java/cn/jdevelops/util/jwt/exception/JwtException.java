package cn.jdevelops.util.jwt.exception;

/**
 * JwtException
 * @author tnnn
 */
public class JwtException extends RuntimeException{
    public JwtException(String message)
    {
        super(message);
    }

    public JwtException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
