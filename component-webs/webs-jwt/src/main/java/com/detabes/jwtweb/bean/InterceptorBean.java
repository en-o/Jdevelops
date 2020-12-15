package com.detabes.jwtweb.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author tn
 * @ClassName InterceptorBean
 * @description 拦截器的放行与拦截
 * @date 2020-09-30 16:58
 */
@ConfigurationProperties(prefix = "databstech.jwt.web.interceptor")
@Component
@Getter
@Setter
@ToString
@Accessors(chain = true)
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
