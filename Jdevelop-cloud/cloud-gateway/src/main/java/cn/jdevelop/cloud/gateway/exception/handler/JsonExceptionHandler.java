package cn.jdevelop.cloud.gateway.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.function.server.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Gateway自定义异常返回 - 序列化
 * @author tn
 * @date  2020-06-04
 */
@Slf4j
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler {


    public JsonExceptionHandler(ErrorAttributes errorAttributes,
                                        ResourceProperties resourceProperties,
                                        ErrorProperties errorProperties,
                                        ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        // 这里其实可以根据异常类型进行定制化逻辑
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, includeStackTrace);
        Throwable error = super.getError(request);
        return response(getHttpStatus(errorAttributes), this.buildMessage(request, error));
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }


    /**
     * 根据code获取对应的HttpStatus
     * @param errorAttributes
     */
    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        int statusCode = 500;
        try {
            try {
                statusCode = (int) errorAttributes.get("status");
            }catch (Exception e){
                statusCode = (int) errorAttributes.get("code");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return statusCode;
    }

    /**
     * 构建异常信息
     */
    private String buildMessage(ServerRequest request, Throwable ex) {
        StringBuilder message = new StringBuilder("Failed to handle request [");
        message.append(request.methodName());
        message.append(" ");
        message.append(request.uri());
        message.append("]");
        if (ex != null) {
            message.append(": ");
            message.append(ex.getMessage());
        }
        return message.toString();
    }




    /**
     * 构建返回的JSON数据格式
     * @param status        状态码
     * @param errorMessage  异常信息
     * @return 返回json数据
     */
    public static Map<String, Object> response(int status, String errorMessage) {
        Map<String, Object> stringObjectMap =new HashMap<>(7);
        stringObjectMap.put("code",status);
        stringObjectMap.put("message",errorMessage);
        stringObjectMap.put("data",null);
        stringObjectMap.put("ts",System.currentTimeMillis());
        stringObjectMap.put("success",false);
        return stringObjectMap;
    }
}