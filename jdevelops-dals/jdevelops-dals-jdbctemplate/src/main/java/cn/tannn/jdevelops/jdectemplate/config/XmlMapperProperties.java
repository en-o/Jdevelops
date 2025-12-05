package cn.tannn.jdevelops.jdectemplate.config;

/**
 * XML Mapper 配置
 * <p>注意：Mapper 接口扫描请使用 @XmlMapperScan 注解</p>
 *
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

    @Override
    public String toString() {
        return "XmlMapperProperties{" +
                "enabled=" + enabled +
                ", locations='" + locations + '\'' +
                '}';
    }
}
