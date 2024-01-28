package cn.jdevelops.sboot.authentication.jwt.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@ConditionalOnMissingBean(InterceptorConfig.class)
public class InterceptorConfig {

    /**
     * 放行 默认放行swagger，/user/login/**等路径
     */
    private Set<String> excludePathPatterns = new HashSet<>();
    /**
     * 拦截 为空拦截所有
     */
    private Set<String> addPathPatterns = new HashSet<>();


    @Override
    public String toString() {
        return "InterceptorBean{" +
                "excludePathPatterns=" + excludePathPatterns +
                ", addPathPatterns=" + addPathPatterns +
                '}';
    }

    public Set<String> getExcludePathPatterns() {
        return excludePathPatterns;
    }

    public void setExcludePathPatterns(Set<String> excludePathPatterns) {
        this.excludePathPatterns = excludePathPatterns;
    }

    public Set<String> getAddPathPatterns() {
        return addPathPatterns;
    }

    public void setAddPathPatterns(Set<String> addPathPatterns) {
        this.addPathPatterns = addPathPatterns;
    }
}
