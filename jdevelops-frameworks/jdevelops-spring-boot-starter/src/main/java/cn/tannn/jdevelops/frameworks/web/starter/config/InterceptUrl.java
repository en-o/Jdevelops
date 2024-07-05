package cn.tannn.jdevelops.frameworks.web.starter.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * UrlService 用到的bean
 * @author tn
 * @date 2020-09-30 16:58
 */
@ConfigurationProperties(prefix = "jdevelops.web.geturl")
@Component
public class InterceptUrl {

    /**
     * 不需要记录的url
     */
    private Set<String> excludePathPatterns = new HashSet<>();

    @Override
    public String toString() {
        return "InterceptUrl{" +
                "excludePathPatterns=" + excludePathPatterns +
                '}';
    }

    public Set<String> getExcludePathPatterns() {
        return excludePathPatterns;
    }

    public void setExcludePathPatterns(Set<String> excludePathPatterns) {
        this.excludePathPatterns = excludePathPatterns;
    }
}
