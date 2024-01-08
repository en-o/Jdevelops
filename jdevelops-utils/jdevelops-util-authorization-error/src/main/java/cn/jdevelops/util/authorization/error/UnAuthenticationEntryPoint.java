package cn.jdevelops.util.authorization.error;

import cn.jdevelops.api.result.emums.ExceptionCode;
import cn.jdevelops.api.result.emums.TokenExceptionCode;
import cn.jdevelops.util.authorization.error.respone.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 * @author tan
 */
public class UnAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private String loginFormUrl;

    /**
     * @param loginFormUrl 登录页面
     */
    public UnAuthenticationEntryPoint(String loginFormUrl) {
        this.loginFormUrl = loginFormUrl;
    }

    private static final Logger logger = LoggerFactory.getLogger(UnAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (response.isCommitted()) {
            return;
        }
        ExceptionCode tokenError = TokenExceptionCode.TOKEN_ERROR;
        if (authException instanceof InvalidBearerTokenException) {
            logger.warn("令牌无效或已过期");
            //如果是api请求类型，则返回json
            ResponseUtil.exceptionResponse(response,tokenError);
        }else if (authException instanceof InsufficientAuthenticationException){
            String accept = request.getHeader("accept");
            if(accept.contains(MediaType.TEXT_HTML_VALUE)){
                //如果是html请求类型，则返回登录页
                LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint =
                        new LoginUrlAuthenticationEntryPoint(loginFormUrl);
                loginUrlAuthenticationEntryPoint.commence(request,response,authException);
            }else {
                //如果是api请求类型，则返回json
                ResponseUtil.exceptionResponse(response,new ExceptionCode(tokenError.getCode(), "令牌不能为空"));
            }
        }else {
            ResponseUtil.exceptionResponse(response, new ExceptionCode(tokenError.getCode(), ResponseUtil.getExceptionMessage(authException)));
        }


    }
}
