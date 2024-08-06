package cn.tannn.jdevelops.pf4j.controller;

import cn.tannn.jdevelops.pf4j.service.PluginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.PluginDescriptor;
import org.pf4j.PluginState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

/**
 * 插件控制器
 *
 * 作者 <a href="https://t.tannn.cn/">tan</a>
 * 版本 V1.0
 * 日期 2024/8/1 上午9:22
 */
@RestController
@RequestMapping("plugins")
@Tag(name = "PluginController", description = "管理和执行插件")
public class PluginController {

    @Autowired
    private PluginService pluginService;


    /**
     * 获取所有插件描述信息
     */
    @Operation(summary = "获取所有插件描述信息")
    @GetMapping("/")
    public List<PluginDescriptor> plugins() {
        return pluginService.pluginsDescriptor();
    }

    /**
     * 获取所有插件ID
     */
    @Operation(summary = "获取所有插件ID")
    @GetMapping("ids")
    public List<String> pluginsId() {
        return pluginService.pluginsId();
    }

    /**
     * 启动插件
     */
    @Operation(summary = "启动插件")
    @PostMapping("/start/{pluginId}")
    public PluginState startPlugin(@PathVariable String pluginId) {
        return pluginService.startPlugin(pluginId);
    }

    /**
     * 启用插件
     */
    @Operation(summary = "启用插件")
    @PostMapping("/enable/{pluginId}")
    public boolean enablePlugin(@PathVariable String pluginId) {
        return pluginService.enablePlugin(pluginId);
    }

    /**
     * 停止插件
     */
    @Operation(summary = "停止插件")
    @PostMapping("/stop/{pluginId}")
    public PluginState stopPlugin(@PathVariable String pluginId) {
        return pluginService.stopPlugin(pluginId);
    }

    /**
     * 禁用插件
     */
    @Operation(summary = "禁用插件")
    @PostMapping("/disable/{pluginId}")
    public boolean disablePlugin(@PathVariable String pluginId) {
        return pluginService.disablePlugin(pluginId);
    }

    /**
     * 加载插件
     */
    @Operation(summary = "加载插件")
    @PostMapping("/load")
    public String loadPlugin(@RequestParam String pluginPath) {
        return pluginService.loadPlugin(pluginPath);
    }

    /**
     * 卸载插件
     */
    @Operation(summary = "卸载插件")
    @PostMapping("/unload/{pluginId}")
    public boolean unloadPlugin(@PathVariable String pluginId) {
        return pluginService.unloadPlugin(pluginId);
    }

    /**
     * 获取插件路径
     */
    @Operation(summary = "获取插件路径")
    @GetMapping("/roots")
    public List<Path> pluginsRoots() {
        return pluginService.pluginsRoots();
    }



}
