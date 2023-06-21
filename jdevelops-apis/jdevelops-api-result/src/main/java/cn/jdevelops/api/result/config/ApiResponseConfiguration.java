package cn.jdevelops.api.result.config;

import cn.jdevelops.api.result.handler.ResultHandlerMethodReturnValueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tan
 */
@ConditionalOnProperty(
        value="jdevelops.api.result.enabled",
        havingValue = "false")
public class ApiResponseConfiguration {


    @Autowired
    public void resetRequestMappingHandlerAdapter(RequestMappingHandlerAdapter requestMappingHandlerAdapter){
        List<HandlerMethodReturnValueHandler> oldReturnValueHandlers =  requestMappingHandlerAdapter.getReturnValueHandlers();
        if (oldReturnValueHandlers != null) {
            ArrayList<HandlerMethodReturnValueHandler> newReturnValueHandlers = new ArrayList<>(oldReturnValueHandlers);
            newReturnValueHandlers.add(0, new ResultHandlerMethodReturnValueHandler());
            requestMappingHandlerAdapter.setReturnValueHandlers(newReturnValueHandlers);
        }
    }



}
