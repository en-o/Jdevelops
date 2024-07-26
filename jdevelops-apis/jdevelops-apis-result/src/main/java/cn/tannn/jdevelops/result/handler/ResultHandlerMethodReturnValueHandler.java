package cn.tannn.jdevelops.result.handler;

import cn.tannn.jdevelops.result.exception.ExceptionResultWrap;
import cn.tannn.jdevelops.result.response.ResultPageVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

/**
 * 设置全局默认的返回结构，如果开启就会强行在返回值中加入内置的返回结构
 *
 * @author tan
 * @see org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor
 */
public class ResultHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    private MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();


    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        // 指定 接口存在ResponseBody注解和返回的不是ExceptionResultWrap类就要做自定义处理，给与包裹类 handleReturnValue 中的处理逻辑
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) ||
                returnType.hasMethodAnnotation(ResponseBody.class))
                && !ExceptionResultWrap.class.equals(returnType.getDeclaringClass())
                && !ResultPageVO.class.equals(returnType.getParameterType())
                && !ExceptionResultWrap.success().getClass().equals(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue,
                                  MethodParameter returnType,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {
        HttpServletResponse response = (HttpServletResponse) webRequest.getNativeResponse();
        if (!Objects.isNull(response)) {
            // 可通过客户端的传递的请求头来切换不同的响应体的内容
            mavContainer.setRequestHandled(true);
            // returnValue =  POJO
            Object apiResponse = ExceptionResultWrap.success(returnValue);
            response.addHeader("version", "1.0");
            ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
            converter.write(apiResponse, MediaType.APPLICATION_JSON, outputMessage);
        }
    }


    protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        Assert.state(response != null, "No HttpServletResponse");
        return new ServletServerHttpResponse(response);
    }
}
