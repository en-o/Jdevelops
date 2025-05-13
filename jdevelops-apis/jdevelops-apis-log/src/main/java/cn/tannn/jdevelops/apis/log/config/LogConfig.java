package cn.tannn.jdevelops.apis.log.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * 日志配置
 * @author tan
 */
@ConfigurationProperties(prefix = "jdevelops.api.log")
public class LogConfig {

    /**
     * 禁用入参的打印[为空不禁止，]
     * <p>intercept-apis: ["/post/v1","/post/v2"]</p>
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
