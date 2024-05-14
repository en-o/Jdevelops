package cn.tannn.jdevelops.exception.imports;

import cn.tannn.jdevelops.exception.ControllerExceptionHandler;
import cn.tannn.jdevelops.exception.config.ExceptionConfig;
import cn.tannn.jdevelops.result.handler.ResultHandlerMethodReturnValueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局异常开关（默认开启，手动关闭请设置 `jdevelops.exception.enabled=false`
 * @author tan
 */
@Configuration
@ConditionalOnProperty(
        value="jdevelops.exception.enabled"
        , havingValue = "true"
        , matchIfMissing = true)
public class ExceptionConfiguration {


    @Bean
    public ExceptionConfig exceptionConfig () {
        return new ExceptionConfig();
    }


    @Bean
    @ConditionalOnMissingBean(name = "controllerExceptionHandler")
    public ControllerExceptionHandler controllerExceptionHandler () {
       return new ControllerExceptionHandler();
    }

}
