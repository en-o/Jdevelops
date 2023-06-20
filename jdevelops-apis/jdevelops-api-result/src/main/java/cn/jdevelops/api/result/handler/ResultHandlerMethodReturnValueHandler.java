package cn.jdevelops.api.result.handler;

import cn.jdevelops.api.result.custom.ExceptionResultWrap;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 设置全局默认的返回结构，如果开启就会强行在返回值中加入内置的返回结构
 * @author tan
 */
public class ResultHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        // 指定 接口存在ResponseBody注解和返回的不是ExceptionResultWrap类就要做自定义处理，给与包裹类 handleReturnValue 中的处理逻辑
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) ||
                returnType.hasMethodAnnotation(ResponseBody.class))
                && !ExceptionResultWrap.class.equals(returnType.getDeclaringClass());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);
        
    }
}
