package cn.jdevelops.config.standalone.scan;

import cn.jdevelops.config.standalone.dao.ConfigsDao;
import cn.jdevelops.config.standalone.properties.ConfigMeta;
import cn.jdevelops.config.standalone.service.ConfigsService;
import cn.jdevelops.config.standalone.service.ConfigsServiceImpl;
import cn.jdevelops.config.standalone.spring.ConfigRegister;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/5/7 10:51
 */
@Import(ConfigRegister.class)
public class ConfigImport {

    @Bean
    public ConfigMeta configMeta() {
        return new ConfigMeta();
    }


    @Bean
    public ConfigsService configsService(ConfigsDao configsDao, ConfigMeta configMeta) {
        return new ConfigsServiceImpl(configsDao, configMeta);
    }
}
