package cn.tannn.jdevelops.ddss;

import cn.tannn.jdevelops.ddss.config.DynamicDataSourceConfig;
import cn.tannn.jdevelops.ddss.config.properties.DynamicDataSourceProperties;
import cn.tannn.jdevelops.ddss.core.DynamicDataSourceAspect;
import org.springframework.context.annotation.Bean;

/**
 * 注入
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/18 下午2:03
 */
public class DdssConfiguration {

    @Bean
    public DynamicDataSourceConfig dynamicDataSourceConfig(){
        return new DynamicDataSourceConfig();
    }

    @Bean
    public DynamicDataSourceAspect dynamicDataSourceAspect(){
        return new DynamicDataSourceAspect();
    }

    @Bean
    public DynamicDataSourceProperties dynamicDataSourceProperties(){
        return new DynamicDataSourceProperties();
    }

}
