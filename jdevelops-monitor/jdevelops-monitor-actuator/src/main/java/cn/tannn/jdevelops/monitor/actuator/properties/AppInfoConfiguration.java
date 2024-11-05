package cn.tannn.jdevelops.monitor.actuator.properties;

import cn.tannn.jdevelops.monitor.actuator.util.ActuatorEnvironmentUtils;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.system.ApplicationPid;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用信息
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/9/4 下午12:34
 */
@ConfigurationProperties("jdevelops.actuator")
public class AppInfoConfiguration {

    /**
     * false 关闭 info显示新增只返回空{}
     */
    String enabled;

    /**
     * 应用名(默认spring.application.name)
     */
    String app;
    /**
     * 应用版本
     */
    String version;
    /**
     * 应用描述
     */
    String description;


    public  Map<String, Object> toMap() {
        Map<String, Object> details = new HashMap<>();
        details.put("app", app==null ? ActuatorEnvironmentUtils.searchByKey("spring.application.name") : app);
        details.put("version", version);
        details.put("description", description);
        details.put("java", System.getProperty("java.version"));
        details.put("port", ActuatorEnvironmentUtils.searchByKey("server.port"));
        details.put("springboot", SpringBootVersion.getVersion());
        details.put("pid", new ApplicationPid().toString());
        details.put("profiles", ActuatorEnvironmentUtils.searchByKey("spring.profiles.active"));
        // 操作系统信息
        details.put("osName", System.getProperty("os.name"));
        details.put("osVersion", System.getProperty("os.version"));
        details.put("osArch", System.getProperty("os.arch"));
        return details;
    }


    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "AppInfoConfiguration{" +
                "enabled='" + enabled + '\'' +
                ", app='" + app + '\'' +
                ", version='" + version + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
