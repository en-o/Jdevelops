package cn.tannn.jdevelops.jdectemplate.config;

/**
 * xml mapper 配置
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/12/4 23:13
 */
public class XmlMapperProperties {

    /**
     * 是否启用 XML Mapper 功能
     */
    Boolean enabled = true;

    /**
     * XML Mapper 文件位置，支持通配符
     */
    String locations = "classpath*:jmapper/**/*.xml";

    /**
     * 扫描 @XmlMapper 接口的包路径
     * <p>如果不配置，则使用 jdevelops.jdbc.base-package</p>
     */
    String basePackages;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public String toString() {
        return "XmlMapperProperties{" +
                "enabled=" + enabled +
                ", locations='" + locations + '\'' +
                ", basePackages='" + basePackages + '\'' +
                '}';
    }
}
