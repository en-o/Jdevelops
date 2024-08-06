package cn.tannn.jdevelops.pf4j.service;

import cn.tannn.jdevelops.pf4j.module.PluginInfo;
import org.pf4j.PluginDescriptor;
import org.pf4j.PluginManager;
import org.pf4j.PluginState;
import org.pf4j.PluginWrapper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 默认的服务
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/8/6 下午3:03
 */
public class DefPluginService<T> implements PluginService{

    private final PluginManager pluginManager;

    public DefPluginService(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public <T> List<T> extensions(Class<T> type, String pluginId) {
        return pluginManager.getExtensions(type, pluginId);
    }

    @Override
    public <T> List<T> extensions(Class<T> type) {
        return pluginManager.getExtensions(type);
    }


    @Override
    public List<PluginWrapper> plugins() {
        return pluginManager.getPlugins();
    }

    @Override
    public List<PluginInfo> pluginsDescriptor() {
        return pluginManager.getPlugins()
                .stream()
                .map( des -> new PluginInfo(des.getPluginState(), des.getDescriptor()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> pluginsId() {
        return pluginManager.getPlugins().stream().map(p -> p.getDescriptor().getPluginId()).collect(Collectors.toList());
    }

    @Override
    public PluginState startPlugin(String pluginId) {
        return pluginManager.startPlugin(pluginId);
    }

    @Override
    public boolean enablePlugin(String pluginId) {
        return pluginManager.enablePlugin(pluginId);
    }

    @Override
    public PluginState stopPlugin(String pluginId) {
        return pluginManager.stopPlugin(pluginId);
    }

    @Override
    public boolean disablePlugin(String pluginId) {
        return pluginManager.disablePlugin(pluginId);
    }

    @Override
    public String loadPlugin(String pluginPath) {
        return pluginManager.loadPlugin(Paths.get(pluginPath));
    }

    @Override
    public boolean unloadPlugin(String pluginId) {
        return pluginManager.unloadPlugin(pluginId);
    }

    @Override
    public List<Path> pluginsRoots() {
        return pluginManager.getPluginsRoots();
    }
}
