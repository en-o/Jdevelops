package cn.jdevelops.config.standalone.spring;

import cn.jdevelops.config.standalone.listener.ConfigChangeListener;
import cn.jdevelops.config.standalone.service.ConfigsService;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * 数据源 获取/监听修改 接口
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/2 下午8:45
 */
public interface ConfigPropertySourceService extends ConfigChangeListener {

    /**
     * 默认 method - 获取存储的数据
     *
     * @param applicationContext {@link ApplicationContext}
     * @param configsService  数据库查询
     * @return TConfigService
     */
    static ConfigPropertySourceService getDefault(ApplicationContext applicationContext
            , ConfigsService configsService) {
        // 存储的数据
        Map<String, String> config = configsService.getConfig();
        ConfigPropertySourceServiceImpl configService = new ConfigPropertySourceServiceImpl(config, applicationContext);
        configsService.addListener(configService);
        return configService;
    }

    /**
     * 配置的所有key
     *
     * @return keys
     */
    String[] getPropertyNames();

    /**
     * 配置的值
     *
     * @param name key
     * @return value
     */
    String getProperty(String name);
}
