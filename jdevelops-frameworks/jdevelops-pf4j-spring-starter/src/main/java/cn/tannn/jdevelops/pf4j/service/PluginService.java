package cn.tannn.jdevelops.pf4j.service;

import cn.tannn.jdevelops.pf4j.module.PluginInfo;
import org.pf4j.PluginDescriptor;
import org.pf4j.PluginManager;
import org.pf4j.PluginState;
import org.pf4j.PluginWrapper;

import java.nio.file.Path;
import java.util.List;

/**
 * 插件服务
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/8/6 下午2:53
 */
public interface PluginService {

    /**
     * 获取插件管理器
     *
     * @return PluginManager
     */
    PluginManager getPluginManager();

    /**
     * 获取指定插件的扩展
     * @param type 插件接口
     * @param pluginId 插件ID
     * @return List plugin
     * @param <T> 插件接口
     */
    <T> List<T> extensions(Class<T> type, String pluginId);

    /**
     * 获取指定插件的扩展
     * @param type 插件接口
     * @return List plugin
     * @param <T> 插件接口
     */
    <T> List<T> extensions(Class<T> type);

    /**
     * 获取所有插件
     *
     * @return PluginWrapper
     */
    List<PluginWrapper> plugins();

    /**
     * 获取所有插件的描述信息
     *
     * @return PluginInfo
     */
    List<PluginInfo> pluginsDescriptor();

    /**
     * 获取所有插件ID
     *
     * @return String
     */
    List<String> pluginsId();

    /**
     * 启动插件  {@link PluginState#STARTED}
     *
     * @param pluginId 插件ID
     * @return PluginState
     */
    PluginState startPlugin(String pluginId);

    /**
     * 启用插件 {@link PluginState#CREATED}
     *
     * @param pluginId 插件ID
     * @return boolean
     */
    boolean enablePlugin(String pluginId);

    /**
     * 停止插件  {@link PluginState#STOPPED}
     *
     * @param pluginId 插件ID
     * @return PluginState
     */
    PluginState stopPlugin(String pluginId);

    /**
     * 禁用插件 - 禁用之后必须使用启动才能生效 {@link PluginState#DISABLED}
     *
     * @param pluginId 插件ID
     * @return boolean
     */
    boolean disablePlugin(String pluginId);

    /**
     * 加载插件
     * <p> 1. 加载前要先写卸载原来的
     * <p> 2. 要启用插件才开始工作
     *
     * @param pluginPath H:/test/pf4j/plugins/HelloPlugin-0.0.2-SNAPSHOT.jar
     * @return String
     */
    String loadPlugin(String pluginPath);

    /**
     * 卸载插件
     *
     * @param pluginId 插件ID
     * @return boolean
     */
    boolean unloadPlugin(String pluginId);

    /**
     * 获取所有插件路径
     *
     * @return Path
     */
    List<Path> pluginsRoots();

}
