package cn.jdevelops.config.standalone.listener;


import org.springframework.cloud.context.properties.ConfigurationPropertiesRebinder;

/**
 * 属性值变化监听器  {@link ConfigurationPropertiesRebinder#onApplicationEvent}
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/4 下午10:48
 */
public interface ConfigChangeListener {

    /**
     * 属性变化监听
     * @param event {@link ConfigChangeEvent}
     */
    void onChange(ConfigChangeEvent event);



}
