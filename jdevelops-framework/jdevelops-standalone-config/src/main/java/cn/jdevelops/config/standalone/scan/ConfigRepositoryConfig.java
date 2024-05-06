package cn.jdevelops.config.standalone.scan;

import cn.jdevelops.config.standalone.dao.ConfigsDao;
import cn.jdevelops.config.standalone.dao.ConfigsDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

public class ConfigRepositoryConfig {

    @Autowired
    private EntityManager entityManager;

    @Bean
    public ConfigsDao configsDao() {
        return new ConfigsDaoImpl(entityManager);
    }
}
