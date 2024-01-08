package cn.jdevelops.util.authorization.error.respone;

import cn.hutool.json.JSONUtil;
import cn.jdevelops.api.result.emums.ExceptionCode;
import cn.jdevelops.api.result.emums.TokenExceptionCode;
import cn.jdevelops.api.result.response.ResultVO;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 返回处理
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/10/8 11:11
 */
public class ResponseUtil {


    public static String getExceptionMessage(Exception e){
        String message;
        if (e instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException o = (OAuth2AuthenticationException) e;
            message = o.getError().getDescription();
        } else {
            message = e.getMessage();
        }
        return  message;
    }


    /**
     * 异常响应  默认
     */
    public static void exceptionResponse(HttpServletResponse response, Exception e)
            throws AccessDeniedException, AuthenticationException, IOException {
        exceptionResponse(response,new ExceptionCode( TokenExceptionCode.TOKEN_ERROR.getCode(),
                ResponseUtil.getExceptionMessage(e)));
    }

    /**
     * 异常响应
     */
    public static void exceptionResponse(HttpServletResponse response, ExceptionCode tokenError)
            throws AccessDeniedException, AuthenticationException, IOException {
        ResultVO<String> fail = ResultVO.of(tokenError);
        String jsonStr = JSONUtil.toJsonStr(fail);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().print(jsonStr);
    }
}
