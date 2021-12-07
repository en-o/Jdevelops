package cn.jdevelops.jwtweb.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 拦截器的放行与拦截
 * @author tn
 * @date 2020-09-30 16:58
 */
@ConfigurationProperties(prefix = "jdevelops.jwt.web.interceptor")
@Component
@Getter
@Setter
@ToString
public class InterceptorBean {

    /**
     * 放行 默认放行swagger，/user/login/**等路径
     */
    private Set<String> excludePathPatterns = new HashSet<>();
    /**
     * 拦截 为空拦截所有
     */
    private Set<String> addPathPatterns = new HashSet<>();
}
