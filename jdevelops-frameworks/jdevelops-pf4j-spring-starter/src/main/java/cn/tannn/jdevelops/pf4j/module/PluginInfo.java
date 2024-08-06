package cn.tannn.jdevelops.pf4j.module;

import io.swagger.v3.oas.annotations.media.Schema;
import org.pf4j.PluginDependency;
import org.pf4j.PluginDescriptor;
import org.pf4j.PluginState;

import java.util.List;

/**
 * 插件描述
 * @see PluginDescriptor
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/8/6 下午3:52
 */
@Schema(description = "插件描述")
public class PluginInfo {

    /**
     * ID
     */
    @Schema(description = "ID")
    String pluginId;

    /**
     * 描述
     */
    @Schema(description = "描述")
    String pluginDescription;

    /**
     * 类
     */
    @Schema(description = "类")
    String pluginClass;

    /**
     * 版本
     */
    @Schema(description = "版本")
    String version;

    /**
     * 必要条件
     */
    @Schema(description = "必要条件")
    String requires;

    /**
     * 提供者
     */
    @Schema(description = "提供者")
    String provider;

    /**
     * 许可证
     */
    @Schema(description = "许可证")
    String license;

    /**
     * 依赖
     */
    @Schema(description = "依赖")
    List<PluginDependency> dependencies;

    /**
     * 状态
     */
    @Schema(description = "状态")
    PluginState pluginState;

    public PluginInfo() {
    }

    public PluginInfo(PluginState pluginState,PluginDescriptor descriptor) {
        this.pluginId = descriptor.getPluginId();
        this.pluginDescription = descriptor.getPluginDescription();
        this.pluginClass = descriptor.getPluginClass();
        this.version = descriptor.getVersion();
        this.requires = descriptor.getRequires();
        this.provider = descriptor.getProvider();
        this.license = descriptor.getLicense();
        this.dependencies = descriptor.getDependencies();
        this.pluginState = pluginState;
    }

    public PluginInfo(String pluginId, String pluginDescription, String pluginClass, String version, String requires, String provider, String license, List<PluginDependency> dependencies, PluginState pluginState) {
        this.pluginId = pluginId;
        this.pluginDescription = pluginDescription;
        this.pluginClass = pluginClass;
        this.version = version;
        this.requires = requires;
        this.provider = provider;
        this.license = license;
        this.dependencies = dependencies;
        this.pluginState = pluginState;
    }

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    public String getPluginDescription() {
        return pluginDescription;
    }

    public void setPluginDescription(String pluginDescription) {
        this.pluginDescription = pluginDescription;
    }

    public String getPluginClass() {
        return pluginClass;
    }

    public void setPluginClass(String pluginClass) {
        this.pluginClass = pluginClass;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRequires() {
        return requires;
    }

    public void setRequires(String requires) {
        this.requires = requires;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public List<PluginDependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<PluginDependency> dependencies) {
        this.dependencies = dependencies;
    }

    public PluginState getPluginState() {
        return pluginState;
    }

    public void setPluginState(PluginState pluginState) {
        this.pluginState = pluginState;
    }

    @Override
    public String toString() {
        return "PluginInfo{" +
                "pluginId='" + pluginId + '\'' +
                ", pluginDescription='" + pluginDescription + '\'' +
                ", pluginClass='" + pluginClass + '\'' +
                ", version='" + version + '\'' +
                ", requires='" + requires + '\'' +
                ", provider='" + provider + '\'' +
                ", license='" + license + '\'' +
                ", dependencies=" + dependencies +
                ", pluginState=" + pluginState +
                '}';
    }
}
