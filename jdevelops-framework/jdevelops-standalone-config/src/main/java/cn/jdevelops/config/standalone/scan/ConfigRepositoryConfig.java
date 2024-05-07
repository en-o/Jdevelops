package cn.jdevelops.config.standalone.scan;

import cn.jdevelops.config.standalone.dao.ConfigsDao;
import cn.jdevelops.config.standalone.dao.ConfigsDaoImpl;
import cn.jdevelops.config.standalone.properties.ConfigMeta;
import cn.jdevelops.config.standalone.service.ConfigsService;
import cn.jdevelops.config.standalone.service.ConfigsServiceImpl;
import cn.jdevelops.config.standalone.spring.ConfigRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

/**
 * scan
 *
 * @author tan
 */

public class ConfigRepositoryConfig {

    @Autowired
    private EntityManager entityManager;

    @Bean
    public ConfigsDao configsDao() {
        return new ConfigsDaoImpl(entityManager);
    }

    @Bean
    public ConfigsService configsService(ConfigsDao configsDao, ConfigMeta configMeta) {
        return new ConfigsServiceImpl(configsDao, configMeta);
    }
}
