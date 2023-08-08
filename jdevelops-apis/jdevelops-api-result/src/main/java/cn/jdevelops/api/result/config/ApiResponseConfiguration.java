package cn.jdevelops.api.result.config;

import cn.jdevelops.api.result.handler.ResultHandlerMethodReturnValueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 隐式添加接口包裹类 （当接口为使用 ResultVO时会自动添加)
 * @author tan
 */
@Configuration
@ConditionalOnProperty(
        value="jdevelops.api.result.enabled",
        havingValue = "true")
public class ApiResponseConfiguration {

    @Autowired
    public void resetRequestMappingHandlerAdapter(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        List<HandlerMethodReturnValueHandler> oldReturnValueHandlers = requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> newReturnValueHandlers = new ArrayList<>(oldReturnValueHandlers);
        newReturnValueHandlers.add(0, new ResultHandlerMethodReturnValueHandler());
        requestMappingHandlerAdapter.setReturnValueHandlers(newReturnValueHandlers);
    }

}
