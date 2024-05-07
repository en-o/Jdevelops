package cn.jdevelops.config.standalone.spring;

import org.springframework.core.env.EnumerablePropertySource;

/**
 * 封装自定义的配置源 （ 元数据来源于数据库
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/2 下午8:44
 */
public class PropertySource extends EnumerablePropertySource<ConfigPropertySourceService> {


    public PropertySource(String name, ConfigPropertySourceService source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        return this.source.getPropertyNames();
    }

    @Override
    public Object getProperty(String name) {
        return this.source.getProperty(name);
    }
}
