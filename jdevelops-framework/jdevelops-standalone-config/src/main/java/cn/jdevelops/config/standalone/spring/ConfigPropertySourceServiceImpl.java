package cn.jdevelops.config.standalone.spring;

import cn.jdevelops.config.standalone.listener.ConfigChangeEvent;
import cn.jdevelops.config.standalone.listener.ConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * 数据源 获取/监听修改 接口实现 （在接口里偷懒了 TConfigService#getDefault)
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/2 下午8:49
 */
@Slf4j
public class ConfigPropertySourceServiceImpl implements ConfigPropertySourceService, ConfigChangeListener {

    Map<String,String> config;

    final ApplicationContext applicationContext;

    public ConfigPropertySourceServiceImpl(Map<String, String> config, ApplicationContext applicationContext) {
        this.config = config;
        this.applicationContext = applicationContext;
    }

    @Override
    public String[] getPropertyNames() {
        return this.config.keySet().toArray(new String[0]);
    }

    @Override
    public String getProperty(String name) {
        return config.get(name);
    }

    @Override
    public void onChange(ConfigChangeEvent event) {
        this.config = event.getConfig();
        if(!config.isEmpty()){
            // 发送 EnvironmentChangeEvent 事件让spring刷新 environment
            log.debug("[TCONFIG] fire an EnvironmentChangeEvent with keys:" + config.keySet());
            applicationContext.publishEvent(new EnvironmentChangeEvent(config.keySet()));
        }
    }
}
