package cn.jdevelops.version.config;

import cn.jdevelops.version.handler.CustomRequestMappingHandlerMapping;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
/**
 * 控制器映射请求定义（URL映射）
 * @author tnnn
 * <a href="https://www.cnblogs.com/amuge/articles/13821162.html">参考</a>
 */
public class WebMvcRegistrationsConfig implements WebMvcRegistrations {

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new CustomRequestMappingHandlerMapping();
    }
}
