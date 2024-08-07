package cn.tannn.jdevelops.pf4j;

import org.pf4j.RuntimeMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = Pf4j3Properties.PREFIX)
public class Pf4j3Properties {

    public static final String PREFIX = "spring.pf4j";

    /**
     * 是否启用
     */
    private boolean enabled = false;

    /**
     * 运行模式：development、 deployment
     * <ol>
     * <li> development 开发模式下，插件被直接加载，而不是从打包的 JAR 文件中加载
     *  <ul>
     *   <li>  在开发模式下，插件通常位于源码目录中，而不是作为打包的 JAR 文件。
     *   <li>  默认情况下，PF4J 会从指定的插件目录中加载插件。这意味着插件可以直接被修改和编译，而无需打包成 JAR 文件
     *  </ul>
     * </ol>
     * <ol>
     *  <li>deployment 加载打包的JAR文件
     *  <ul>
     *   <li> 在部署模式下，插件通常被打包成 JAR 文件，并放置在指定的插件目录中。
     *   <li> PF4J 会从插件目录中扫描并加载这些 JAR 文件。
     *  </ul>
     * </ol>
     *
     * @see <a href="https://pf4j.org/doc/development-mode.html">development-mode</a>
     * @see <a href="https://www.yuque.com/tanning/mbquef/gqed6ormkahepucp">RuntimeMode</a>
     */
    private RuntimeMode runtimeMode = RuntimeMode.DEPLOYMENT;

    /**
     * 扩展插件目录：可配置绝对路径和相对路径 可选项 【默认项目root\plugins】
     * <p> windows: G:/项目/stest/pf4j/plugins/
     */
    private String path = "src/main/plugins";


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public RuntimeMode getRuntimeMode() {
        return this.runtimeMode;
    }

    public void setRuntimeMode(RuntimeMode runtimeMode) {
        this.runtimeMode = runtimeMode;
    }

    @Override
    public String toString() {
        return "Pf4j3Properties{" +
                "enabled=" + enabled +
                ", runtimeMode=" + runtimeMode +
                ", path='" + path + '\'' +
                '}';
    }
}
