package cn.tannn.jdevelops.autoschema.scan;

import cn.tannn.jdevelops.autoschema.DatabaseInitializer;
import cn.tannn.jdevelops.autoschema.properties.DataBaseProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库自动初始化配置
 * <p>默认不启用，需要通过以下方式启用：
 * <p> 1. 使用注解方式启用{@link EnableAutoSchema}
 * <p> 2. 通过配置 jdevelops.autoschema.enabled=true 启用
 * <p>
 * <p>在启用的情况下可以通过 jdevelops.database.enabled=false 关闭自动建库
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/18 下午2:42
 */
@Configuration
@ConditionalOnProperty(name = "jdevelops.autoschema.enabled", havingValue = "true")
public class AutoSchemaConfiguration {

    /**
     * 数据库配置属性
     */
    @Bean
    @ConditionalOnMissingBean(DataBaseProperties.class)
    public DataBaseProperties dataBaseProperties() {
        return new DataBaseProperties();
    }

    /**
     * 数据库初始化器 - 使用ApplicationReadyEvent替代BeanPostProcessor
     */
    @Bean
    @ConditionalOnMissingBean(DatabaseInitializer.class)
    public DatabaseInitializer databaseInitializer() {
        return new DatabaseInitializer();
    }
}
