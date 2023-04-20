package cn.jdevelops.util.jwt.exception;

/**
 * JwtException
 * @author tnnn
 */
public class LoginException extends RuntimeException{
    public LoginException(String message)
    {
        super(message);
    }

    public LoginException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
