package cn.tannn.jdevelops.apis.log.config;


import cn.tannn.jdevelops.apis.log.ApiLogSave;
import cn.tannn.jdevelops.apis.log.DefApiLogSave;
import cn.tannn.jdevelops.apis.log.aspect.ApiLogAspectSave;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.HashSet;
import java.util.Set;

/**
 * 日志配置
 * @author tan
 */
@ConfigurationProperties(prefix = "jdevelops.api.log")
public class LogConfig {

    /**
     * 拦截 为空拦截所有
     */
    private Set<String> interceptApis = new HashSet<>();

    public LogConfig() {
    }

    public LogConfig(Set<String> interceptApis) {
        this.interceptApis = interceptApis;
    }

    public Set<String> getInterceptApis() {
        return interceptApis;
    }

    public void setInterceptApis(Set<String> interceptApis) {
        this.interceptApis = interceptApis;
    }

    @Override
    public String toString() {
        return "LogConfig{" +
                "interceptApis=" + interceptApis +
                '}';
    }
}
