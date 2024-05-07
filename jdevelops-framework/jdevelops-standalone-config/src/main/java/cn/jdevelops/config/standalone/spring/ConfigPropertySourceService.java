package cn.jdevelops.config.standalone.spring;

import cn.jdevelops.config.standalone.listener.ConfigChangeListener;

/**
 * 数据源 获取/监听修改 接口
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/2 下午8:45
 */
public interface ConfigPropertySourceService extends ConfigChangeListener {



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
