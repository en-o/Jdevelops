package cn.jdevelops.config.standalone.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 客户端注册信息 - 自定义的配置源基础信息
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/5/7 9:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "jdevelops.config")
public class ConfigMeta {
    /**
     * 应用
     */
    private String app;
    /**
     * 环境
     */
    private String env;
    /**
     * namespace
     */
    private String ns;

    public String getApp() {
        return app==null?"app1":app;
    }

    public String getEnv() {
        return env==null?"dev":env;
    }

    public String getNs() {
        return ns==null?"public":ns;
    }
}
